package com.chrisgilbert.checkout.domain

import com.chrisgilbert.checkout.annotation.VisibleForTesting
import java.util.UUID.randomUUID

/**
 * Represents the only type of special offer available in this supermarket - a multi-buy offer
 * where a discount is given when a number of the same item is purchased.
 */
@VisibleForTesting
data class SpecialOffer(
    val id: String = randomUUID().toString(),
    val stockItem: StockItem,
    val bundleQuantity: Int,
    val bundlePrice: Int
) : PricingStrategy {

    /**
     * Apply the offer to as many bundles as are present, pricing the remainder as single items.
     */
    override fun priceOf(quantity: Int) =
        ((quantity / bundleQuantity * bundlePrice)
                + stockItem.priceOf(quantity % bundleQuantity))

}