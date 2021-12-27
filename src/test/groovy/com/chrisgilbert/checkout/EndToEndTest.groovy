package com.chrisgilbert.checkout

import com.chrisgilbert.checkout.dto.Sterling
import com.chrisgilbert.checkout.mapper.BasketMapper
import com.chrisgilbert.checkout.mapper.SterlingMapper
import com.chrisgilbert.checkout.repository.BasketRepository
import com.chrisgilbert.checkout.repository.SpecialOfferRepository
import com.chrisgilbert.checkout.repository.StockItemRepository
import com.chrisgilbert.checkout.service.CheckoutService
import com.chrisgilbert.checkout.service.StockPriceManagementService
import com.chrisgilbert.checkout.web.CheckoutController
import spock.lang.Specification

class EndToEndTest extends Specification {

    def "Checkout a typical basket"() {

        given: 'a store of stock items and special offers'
        def stockItemRepository = new StockItemRepository()
        def specialOfferRepository = new SpecialOfferRepository()
        def priceManagementService = new StockPriceManagementService(
                stockItemRepository, specialOfferRepository
        )

        and: 'a set of stored stock items'
        with(priceManagementService) {
            addStockItem('A', 50)
            addStockItem('B', 30)
            addStockItem('C', 20)
            addStockItem('D', 15)
        }

        and: 'some special offers on some of them'
        with(priceManagementService) {
            addSpecialOffer('A', 3, 130)
            addSpecialOffer('B', 2, 45)
        }

        and: 'an API to access the checkout process'
        def checkoutService = new CheckoutService(
                stockItemRepository, specialOfferRepository, new BasketRepository()
        )
        def checkoutController = new CheckoutController(checkoutService, new BasketMapper(), new SterlingMapper())

        when: 'a new checkout is started'
        def basketId = checkoutController.createBasket().basketId

        and: 'a selection of items is added to the basket'
        with(checkoutController) {
            scanItem('A', basketId)
            scanItem('D', basketId)
            scanItem('A', basketId)
            scanItem('A', basketId)
            scanItem('B', basketId)
            scanItem('A', basketId)
            scanItem('C', basketId)
            scanItem('B', basketId)
            scanItem('C', basketId)
            scanItem('C', basketId)
        }

        and: 'the total price is calculated'
        def finalBill = checkoutController.getTotalPrice(basketId)

        then: 'the total is correct, accounting for special offers'
        // A -> 130 + 50
        // B -> 45
        // C -> 3 * 20
        // D -> 1 * 15
        finalBill == new Sterling(3, 0)

    }

    def "Checkout a typical basket - offers modified during checkout"() {

        given: 'a store of stock items and special offers'
        def stockItemRepository = new StockItemRepository()
        def specialOfferRepository = new SpecialOfferRepository()
        def priceManagementService = new StockPriceManagementService(
                stockItemRepository, specialOfferRepository
        )

        and: 'a set of stored stock items'
        with(priceManagementService) {
            addStockItem('A', 50)
            addStockItem('B', 30)
            addStockItem('C', 20)
            addStockItem('D', 15)
        }

        and: 'some special offers on some of them'
        with(priceManagementService) {
            addSpecialOffer('A', 3, 130)
            addSpecialOffer('B', 2, 45)
        }

        and: 'an API to access the checkout process'
        def checkoutService = new CheckoutService(
                stockItemRepository, specialOfferRepository, new BasketRepository()
        )
        def checkoutController = new CheckoutController(checkoutService, new BasketMapper(), new SterlingMapper())

        when: 'a new checkout is started'
        def basketId = checkoutController.createBasket().basketId

        and: 'a selection of items is added to the basket'
        with(checkoutController) {
            scanItem('A', basketId)
            scanItem('D', basketId)
            scanItem('A', basketId)
            scanItem('A', basketId)
            scanItem('B', basketId)
            scanItem('A', basketId)
            scanItem('C', basketId)
            scanItem('B', basketId)
            scanItem('C', basketId)
            scanItem('C', basketId)
        }

        and: 'a new offer is applied to one of the items'
        priceManagementService.addSpecialOffer('C', 3, 50)

        and: 'the total price is calculated'
        def finalBill = checkoutController.getTotalPrice(basketId)

        then: 'the total is not based on the addedd offer'
        // A -> 130 + 50
        // B -> 45
        // C -> 3 * 20
        // D -> 1 * 15
        finalBill == new Sterling(3, 0)

    }

}
