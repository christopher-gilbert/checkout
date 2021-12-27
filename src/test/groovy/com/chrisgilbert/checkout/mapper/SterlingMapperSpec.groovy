package com.chrisgilbert.checkout.mapper

import com.chrisgilbert.checkout.dto.Sterling
import spock.lang.Specification

class SterlingMapperSpec extends Specification {

    def "Less than a pound"() {

        given: 'less than 100 pennies'
        def pennies = 35

        when: 'they are mapped to a Sterling amount'
        def result = new SterlingMapper().penceToSterling(pennies)

        then: 'there are no pounds and the right number of pence'
        result == new Sterling(0, 35)

    }

    def "No pennies"() {

        given: 'a number of pennies that add up to an exact number of pounds'
        def pennies = 600

        when: 'they are mapped to a Sterling amount'
        def result = new SterlingMapper().penceToSterling(pennies)

        then: 'there are no pennies and the right number of pounds'
        result == new Sterling(6, 0)

    }

    def "Pounds and pence"() {

        given: 'more than 100 pennies that do not add up to an exact number of pounds'
        def pennies = 1797

        when: 'they are mapped to a Sterling amount'
        def result = new SterlingMapper().penceToSterling(pennies)

        then: 'there are the right number of pennies and the right number of pounds'
        result == new Sterling(17, 97)

    }

}
