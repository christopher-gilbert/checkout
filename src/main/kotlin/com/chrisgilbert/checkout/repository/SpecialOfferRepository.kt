package com.chrisgilbert.checkout.repository

import com.chrisgilbert.checkout.annotation.VisibleForTesting
import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import org.springframework.stereotype.Repository

@Repository
@VisibleForTesting
class SpecialOfferRepository(
    private val currentOffers: MutableList<SpecialOffer> = mutableListOf()
) {

    /**
     * Store the offer, and return the stored version.
     * (note that as special offers are immutable it is safe to return the
     * actual stored version)
     */
    fun save(specialOffer: SpecialOffer): SpecialOffer {
        currentOffers.add(specialOffer)
        return specialOffer
    }

    /**
     * Return a list of [SpecialOffers][SpecialOffer] in place at the time of
     * retrieval, safe from any subsequent modification.
     */
    fun getCurrentSpecialOffers() = currentOffers.toList()

    /**
     * Return the [SpecialOffer] associated with the [StockItem] with the passed in SKU or null if none is found.
     */
    fun findOfferForStockItem(sku: String) = currentOffers.find { it.stockItem.sku == sku }
}