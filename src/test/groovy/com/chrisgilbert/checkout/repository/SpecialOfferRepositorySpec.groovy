package com.chrisgilbert.checkout.repository

import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import spock.lang.Specification

class SpecialOfferRepositorySpec extends Specification {

    def "Store offer, return stored version"() {

        given: 'an offer'
        def offer = new SpecialOffer('id1', new StockItem('id2', 'sku1', 10), 2, 15)

        when: 'it is saved'
        def repository = new SpecialOfferRepository([])
        def storedOffer = repository.save(offer)

        then: 'it is stored in the database'
        repository.currentSpecialOffers.any { it == storedOffer }

    }

    def "Retrieved offers with subsequent updates"() {

        given: 'a set of offers retrieved from the repository'
        def offer1 = Stub(SpecialOffer)
        def offer2 = Stub(SpecialOffer)
        def offer3 = Stub(SpecialOffer)
        def repository = new SpecialOfferRepository([offer1, offer2, offer3])
        def currentOffers = repository.currentSpecialOffers

        when: 'the stored offers are amended'
        def offer4 = Stub(SpecialOffer)
        repository.save(offer4)

        then: 'the retrieved set are unchanged'
        currentOffers.size() == 3
        repository.getCurrentSpecialOffers().size() == 4

    }

    def "Retrieve offer by SKU - offer found"() {

        given: 'a set of special offers on a number of items'
        def offer1 = new SpecialOffer('id1', new StockItem('id2', 'sku1', 10), 2, 15)
        def offer2 = new SpecialOffer('id3', new StockItem('id4', 'sku2', 20), 3, 50)

        when: 'looking up an offer by one of the item SKUs'
        def result = new SpecialOfferRepository([offer1, offer2]).findOfferForStockItem('sku2')

        then: 'the relevant offer is found'
        result == offer2

    }

    def "Retrieve offer by SKU - no offer found"() {

        given: 'a set of special offers on a number of items'
        def offer1 = new SpecialOffer('id1', new StockItem('id2', 'sku1', 10), 2, 15)
        def offer2 = new SpecialOffer('id3', new StockItem('id4', 'sku2', 20), 3, 50)

        when: 'looking up an offer for a different SKU'
        def result = new SpecialOfferRepository([offer1, offer2]).findOfferForStockItem('sku3')

        then: 'nothing is found'
        result == null

    }

}
