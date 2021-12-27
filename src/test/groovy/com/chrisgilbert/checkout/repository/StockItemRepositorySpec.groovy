package com.chrisgilbert.checkout.repository


import com.chrisgilbert.checkout.domain.StockItem
import spock.lang.Specification

class StockItemRepositorySpec extends Specification {

    def "Store item, return stored version"() {

        given: 'an item'
        def item = new StockItem('id1', 'sku1', 10)

        when: 'it is saved'
        def repository = new StockItemRepository([])
        def storedItem = repository.save(item)

        then: 'it is stored in the database'
        repository.stockItems.any { it == storedItem }

    }

    def "Retrieve item by SKU - item found"() {

        given: 'a set of stock items'
        def item1 = new StockItem('id1', 'sku1', 10)
        def item2 = new StockItem('id2', 'sku2', 20)

        when: 'looking up an item by one of the item SKUs'
        def result = new StockItemRepository([item1, item2]).findBySku('sku2')

        then: 'the relevant item is found'
        result == item2

    }

    def "Retrieve item by SKU - no offer found"() {

        given: 'a set of stock items'
        def item1 = new StockItem('id1', 'sku1', 10)
        def item2 = new StockItem('id2', 'sku2', 20)

        when: 'looking up an item by a different SKU'
        def result = new StockItemRepository([item1, item2]).findBySku('sku3')

        then: 'nothing is found'
        result == null

    }
}
