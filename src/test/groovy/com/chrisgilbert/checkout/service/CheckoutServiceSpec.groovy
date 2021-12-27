package com.chrisgilbert.checkout.service

import com.chrisgilbert.checkout.domain.Basket
import com.chrisgilbert.checkout.domain.PricingRules
import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import com.chrisgilbert.checkout.repository.BasketRepository
import com.chrisgilbert.checkout.repository.SpecialOfferRepository
import com.chrisgilbert.checkout.repository.StockItemRepository
import spock.lang.Specification

class CheckoutServiceSpec extends Specification {

    def "Start a new checkout"() {

        given: 'a set of special offers'
        def offer1 = Stub(SpecialOffer)
        def offer2 = Stub(SpecialOffer)
        def specialOfferRepository = Stub(SpecialOfferRepository) {
            it.currentSpecialOffers >> [offer1, offer2]
        }

        and: 'storage for baskets'
        def basketRepository = Stub(BasketRepository) {
            save(_ as Basket) >> { args -> args[0] }
        }

        when: 'a new checkout is started'
        def basket = new CheckoutService(Stub(StockItemRepository), specialOfferRepository, basketRepository)
                .startCheckout()

        then: 'an empty basket is created with those special offers'
        basket.applicablePricingRules == new PricingRules([offer1, offer2])
        basket.items == []
    }

    def "Empty basket - known item scanned"() {

        given: 'an empty basket'
        def basket = new Basket('id1', Stub(PricingRules), [])
        def basketRepository = Stub(BasketRepository) {
            findById('id1') >> basket
        }

        and: 'an item with a known SKU'
        def item = Stub(StockItem)
        def stockItemRepository = Stub(StockItemRepository) {
            it.findBySku('SKU') >> item
        }

        when: 'the SKU is scanned'
        basketRepository.save(basket) >> basket
        def updatedBasket = new CheckoutService(stockItemRepository, Stub(SpecialOfferRepository), basketRepository)
                .addItem('SKU', 'id1')

        then: 'the item is added to the basket'
        updatedBasket.items == [item]

    }

    def "Basket with existing items - known item scanned"() {

        given: 'a non-empty basket'
        def item1 = Stub(StockItem)
        def item2 = Stub(StockItem)
        def basket = new Basket('id1', Stub(PricingRules), [])
                .addItem(item1)
                .addItem(item2)
        def basketRepository = Mock(BasketRepository) {
            findById('id1') >> basket
        }

        and: 'an item with a known SKU'
        def item3 = Stub(StockItem)
        def stockItemRepository = Stub(StockItemRepository) {
            it.findBySku('SKU') >> item3
        }

        when: 'the SKU is scanned'
        basketRepository.save(basket) >> basket
        def updatedBasket = new CheckoutService(stockItemRepository, Stub(SpecialOfferRepository), basketRepository)
                .addItem('SKU', 'id1')

        then: 'the item is added to the basket'
        updatedBasket.items == [item1, item2, item3]
    }

    def "Unknown item scanned"() {

        given: 'an empty basket'
        def basket = new Basket('id1', Stub(PricingRules), [])
        def basketRepository = Mock(BasketRepository) {
            findById('id1') >> basket
        }

        and: 'an unknown SKU'
        def stockItemRepository = Stub(StockItemRepository) {
            it.findBySku('SKU') >> null
        }

        when: 'the SKU is scanned'
        new CheckoutService(stockItemRepository, Stub(SpecialOfferRepository), basketRepository)
                .addItem('SKU', 'id1')

        then: 'an exception is thrown'
        thrown(IllegalArgumentException)

    }

    def "Calculating price of basket"() {

        given: 'some items with simple pricing strategies'
        def item1 = Stub(StockItem) {
            priceOf(3) >> 7
        }
        def item2 = Stub(StockItem) {
            priceOf(5) >> 11
        }
        def item3 = Stub(StockItem) {
            priceOf(2) >> 19

        }

        and: 'a stored basket containing those items in expected quantities'
        def basket = Stub(Basket) {
            it.id >> 'id1'
            getSummary() >> [(item1): 3,
                             (item2): 5,
                             (item3): 2]
            getPricingStrategy(_ as StockItem) >> { args -> args[0] }
        }
        def basketRepository = Stub(BasketRepository) {
            findById('id1') >> basket
        }

        when: 'the baskeet total is calculated'
        def total = new CheckoutService(Stub(StockItemRepository),
                Stub(SpecialOfferRepository),
                basketRepository).calculateTotalPrice('id1')

        then: 'it is correct based on the rules and pricing strategy and basket contents'
        total == 7 + 11 + 19

    }

}
