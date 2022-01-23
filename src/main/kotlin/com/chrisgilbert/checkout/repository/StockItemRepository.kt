package com.chrisgilbert.checkout.repository

import com.chrisgilbert.checkout.annotation.VisibleForTesting
import com.chrisgilbert.checkout.domain.StockItem
import org.springframework.stereotype.Repository

@Repository
@VisibleForTesting
class StockItemRepository(
    private val stockItems: MutableList<StockItem> = mutableListOf()
) {

    /**
     * Store the stock item and return the stored version.
     * (note that as stock items are immutable it is safe to return the
     * actual stored version)
     */
    fun save(stockItem: StockItem): StockItem {
        stockItems.add(stockItem)
        return stockItem
    }

    /**
     * Return the [StockItem] with the passed in SKU, or null if not found.
     */
    fun findBySku(sku: String) = stockItems.find {
        it.sku == sku
    }

    /**
     * Return all current stock items.
     */
    fun findAll() = stockItems

}