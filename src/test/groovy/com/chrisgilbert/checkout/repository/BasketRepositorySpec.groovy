package com.chrisgilbert.checkout.repository

import com.chrisgilbert.checkout.domain.Basket
import com.chrisgilbert.checkout.domain.PricingRules
import com.chrisgilbert.checkout.domain.StockItem
import spock.lang.Specification

class BasketRepositorySpec extends Specification {

    def "Safely save new basket"() {

        given: 'a basket'
        def input = new Basket('id1', Stub(PricingRules), [])

        when: 'it is stored'
        def repository = new BasketRepository()
        def output = repository.save(input)

        then: 'the input and output are detached from the stored version'
        def stored = repository.findById('id1')
        !input.is(stored)
        !output.is(stored)

        and: 'the stored basket is the same as the input'
        stored == input

    }


    def "Safely update basket"() {

        given: 'a basket'
        def input = new Basket('id1', Stub(PricingRules), [])

        and: 'a stored basket with the same ID'
        def repository = new BasketRepository(['id1': new Basket('id1', Stub(PricingRules), [Stub(StockItem)])])

        when: 'the stored basket is updated'
        def output = repository.save(input)

        then: 'the input and output are detached from the stored version'
        def stored = repository.findById('id1')
        !input.is(stored)
        !output.is(stored)

        and: 'the stored basket is the same as the input'
        stored == input

    }

    def "Safely retrieve basket"() {

        given: 'a stored basket'
        def stored = new Basket('id1', Stub(PricingRules), [Stub(StockItem)])
        def repository = new BasketRepository(['id1': stored])

        when: 'it is retrieved'
        def output = repository.findById('id1')

        then: 'the output is detached from the stored version'
        !output.is(stored)
    }

}
