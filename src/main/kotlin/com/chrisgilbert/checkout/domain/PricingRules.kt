package com.chrisgilbert.checkout.domain

import com.chrisgilbert.checkout.annotation.VisibleForTesting

/**
 * Business rule factory for finding the correct pricing strategy for a [StockItem] in light of any applicable offers.
 */
@VisibleForTesting
data class PricingRules(
    private val applicableOffers: List<SpecialOffer>
) {

    /**
     * Retrieve the [PricingStrategy] that enables the total cost for a quantity of the passed in
     * [StockItem] to be calculated, accounting for any applicable [SpecialOffers][SpecialOffer].
     */
    fun getPricingStrategy(stockItem: StockItem) =
        applicableOffers.find {
            it.stockItem == stockItem
        } ?: stockItem

}