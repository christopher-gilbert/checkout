package com.chrisgilbert.checkout.domain

import spock.lang.Specification

class SpecialOfferSpec extends Specification {

    def "Pricing of items on offer"() {

        expect: 'special offer pricing strategy to be applied'
        def totalPrice = new SpecialOffer('id1', new StockItem('id1', 'A', unitPrice), offerQuantity, offerPrice)
                .priceOf(quantity)
        totalPrice == expectedTotal

        where: 'the following offers and unit prices are applicable'
        quantity | unitPrice | offerQuantity | offerPrice | expectedTotal
        1        | 20        | 2             | 30         | 20
        2        | 20        | 2             | 30         | 30
        3        | 20        | 2             | 30         | 50

    }
}
