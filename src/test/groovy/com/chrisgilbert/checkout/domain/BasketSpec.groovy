package com.chrisgilbert.checkout.domain


import spock.lang.Specification

class BasketSpec extends Specification {

    def "Add item - empty basket"() {

        given: 'an empty basket'
        def basket = new Basket('id', Stub(PricingRules), [])

        when: 'an item is added'
        def item = Stub(StockItem)
        def updatedBasket = basket.addItem(item)

        then: 'the basket contains the added item'
        updatedBasket.items == [item]

    }

    def "Add item - non-empty basket"() {

        given: 'a basket already containing an item'
        def item1 = Stub(StockItem)
        def basket = new Basket('id', Stub(PricingRules), [item1])

        when: 'another item is added'
        def item2 = Stub(StockItem)
        def updatedBasket = basket.addItem(item2)

        then: 'the basket contains both items'
        updatedBasket.items == [item1, item2]

    }

    def "Summary view of basket"() {

        given: 'a basket containing several items, some identical'
        def itemType1 = new StockItem('id1', 'sku1', 10)
        def itemType2 = new StockItem('id2', 'sku2', 20)
        def basket = new Basket('id', Stub(PricingRules), [itemType1, itemType1, itemType2, itemType1, itemType2, itemType2, itemType1])

        when: 'a summary view is requested'
        def summary = basket.getSummary()

        then: 'it includes correct quantities of distinct item types'
        summary.size() == 2
        summary[itemType1] == 4
        summary[itemType2] == 3

    }

    def "Copy basket"() {

        given: 'a basket'
        def item1 = new StockItem('id1', 'sku1', 45)
        def specialOffer1 = new SpecialOffer('id2', item1, 3, 120)
        def originalBasket = new Basket('id3', new PricingRules([specialOffer1]), [item1])

        and: 'a copy of the basket'
        def copyBasket = originalBasket.copy()

        when: 'the original is modified (in the only way allowed)'
        originalBasket.addItem(Stub(StockItem))

        then: 'the copy is unchanged'
        copyBasket == new Basket('id3', new PricingRules([specialOffer1]), [item1])
    }
}
