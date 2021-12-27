package com.chrisgilbert.checkout.domain

import com.chrisgilbert.checkout.annotation.VisibleForTesting
import java.util.UUID.randomUUID

/**
 * Class for managing the state of checked out [StockItems][StockItem] during the checkout process.
 * Additionally, each [Basket] is associated with a set of [PricingRules] that determine the total cost
 * of each [StockItem] in the [Basket] after any [SpecialOffers][SpecialOffer] are applied.
 */
@VisibleForTesting
data class Basket(
    val id: String = randomUUID().toString(),
    private val applicablePricingRules: PricingRules,
    val items: MutableList<StockItem> = mutableListOf()
) {

    /**
     * Return the [Basket] updated with the new [StockItem] added.
     */
    fun addItem(stockItem: StockItem): Basket {
        items.add(stockItem)
        return this
    }

    /**
     * Provide a summary map of distinct [StockItems][StockItem] to the number of each in the basket.
     */
    fun getSummary() = items.groupingBy { it }.eachCount()

    /**
     * Use the [PricingRules] tied to this basket to retrieve the appropriate pricing strategy for the
     * given [StockItem].
     */
    fun getPricingStrategy(stockItem: StockItem) = applicablePricingRules.getPricingStrategy(stockItem)

    /**
     * Create a copy of the basket with a deep copy of properties that could be modified, for safety.
     */
    fun copy() = copy(
        items = this.items
            .toMutableList()
    )

}
