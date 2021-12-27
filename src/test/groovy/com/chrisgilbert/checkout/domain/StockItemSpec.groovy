package com.chrisgilbert.checkout.domain

import spock.lang.Specification

class StockItemSpec extends Specification {

    def "Pricing of stock items"() {

        given: 'a quantity of a stock item'
        def item = new StockItem('id', 'sku', 34)

        when: 'the price is calculated'
        def price = item.priceOf(12)

        then: 'it is just related to quantity and item price'
        price == 12 * 34

    }
}
