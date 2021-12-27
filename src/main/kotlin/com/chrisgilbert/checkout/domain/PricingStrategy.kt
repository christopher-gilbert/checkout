package com.chrisgilbert.checkout.domain

import com.chrisgilbert.checkout.service.CheckoutService

/**
 * Represents the strategy for calculating a price for a quantity of any type of item.
 * See [StockItem] and [SpecialOffer] for example implementations, and [CheckoutService] for example usage.
 */
interface PricingStrategy {


    /**
     * Return the price for a quantity of items as a simple amount of some (unspecified) currency.
     */
    fun priceOf(quantity: Int): Int

}