package com.chrisgilbert.checkout.service

import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import com.chrisgilbert.checkout.exception.DuplicateItemException
import com.chrisgilbert.checkout.exception.MissingItemException
import com.chrisgilbert.checkout.repository.SpecialOfferRepository
import com.chrisgilbert.checkout.repository.StockItemRepository
import spock.lang.Specification

class StockPriceManagementServiceSpec extends Specification {

    def "Add a stock item"() {

        given: 'a set of details for a stock item'
        def sku = 'sku1'
        def price = 10

        and: 'a repository for storing stock items, that does not contain the given SKU'
        def storedStockItem = Stub(StockItem)
        def repository = Stub(StockItemRepository) {
            findBySku(sku) >> null
            save(_ as StockItem) >> storedStockItem
        }

        when: 'item details are added to the system'
        def result = new StockPriceManagementService(repository, Stub(SpecialOfferRepository)).addStockItem(sku, price)

        then: 'a stored item is returned'
        result == storedStockItem
    }

    def "Add a duplicate item"() {

        given: 'a set of details for a stock item'
        def sku = 'sku1'
        def price = 10

        and: 'a repository that already contains an item with the given SKU'
        def repository = Mock(StockItemRepository) {
            findBySku(sku) >> Stub(StockItem)
        }

        when: 'item details are added to the system'
        new StockPriceManagementService(repository, Stub(SpecialOfferRepository)).addStockItem(sku, price)

        then: 'the duplicate is detected'
        thrown(DuplicateItemException)

        and: 'nothing is stored'
        0 * repository.save(_ as StockItem)
    }

    def "Add a special offer"() {

        given: 'a set of details for a special offer'
        def itemSku = 'sku1'
        def bundleQuantity = 2
        def bundlePrice = 10

        and: 'an existing item for the given SKU'
        def stockItemRepository = Stub(StockItemRepository) {
            findBySku(itemSku) >> Stub(StockItem)
        }

        and: 'a repository for storing special offers that does not contain an offer for the item with the given SKU'
        def storedOffer = Stub(SpecialOffer)
        def specialOfferRepository = Stub(SpecialOfferRepository) {
            findOfferForStockItem(itemSku) >> null
            save(_ as SpecialOffer) >> storedOffer
        }

        when: 'offer details are added to the system'
        def result = new StockPriceManagementService(stockItemRepository, specialOfferRepository)
                .addSpecialOffer(itemSku, bundleQuantity, bundlePrice)

        then: 'a stored offer is returned'
        result == storedOffer
    }

    def "Add a special offer to an item with an existing offer"() {

        given: 'a set of details for a special offer'
        def itemSku = 'sku1'
        def bundleQuantity = 2
        def bundlePrice = 10

        and: 'an existing item for the given SKU'
        def stockItemRepository = Stub(StockItemRepository) {
            findBySku(itemSku) >> Stub(StockItem)
        }

        and: 'a repository for storing special offers that already contains an offer for the item with the given SKU'
        def specialOfferRepository = Mock(SpecialOfferRepository) {
            findOfferForStockItem(itemSku) >> Stub(SpecialOffer)
        }

        when: 'offer details are added to the system'
        new StockPriceManagementService(stockItemRepository, specialOfferRepository)
                .addSpecialOffer(itemSku, bundleQuantity, bundlePrice)

        then: 'the duplicate is detected'
        thrown(DuplicateItemException)

        and: 'nothing is stored'
        0 * specialOfferRepository.save(_ as SpecialOffer)
    }

    def "Add a special offer to an unknown item"() {

        given: 'a set of details for a special offer'
        def itemSku = 'sku1'
        def bundleQuantity = 2
        def bundlePrice = 10

        and: 'no existing item for the given SKU'
        def stockItemRepository = Stub(StockItemRepository) {
            findBySku(itemSku) >> null
        }

        and: 'a repository for storing special offers'
        def specialOfferRepository = Mock(SpecialOfferRepository)

        when: 'offer details are added to the system'
        new StockPriceManagementService(stockItemRepository, specialOfferRepository)
                .addSpecialOffer(itemSku, bundleQuantity, bundlePrice)

        then: 'the missing item is detected'
        thrown(MissingItemException)

        and: 'nothing is stored'
        0 * specialOfferRepository.save(_ as SpecialOffer)
    }


}
