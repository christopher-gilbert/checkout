package com.chrisgilbert.checkout.service

import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import com.chrisgilbert.checkout.exception.DuplicateItemException
import com.chrisgilbert.checkout.exception.MissingItemException
import com.chrisgilbert.checkout.repository.SpecialOfferRepository
import com.chrisgilbert.checkout.repository.StockItemRepository
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

/**
 * Service for safely managing [StockItems][StockItem] and [SpecialOffers][SpecialOffer], ensuring the integrity of
 * stored data.
 */
@Service
class StockPriceManagementService(
    private val stockItemRepository: StockItemRepository,
    private val specialOfferRepository: SpecialOfferRepository
) {

    /**
     * Create and store a new [StockItem] returning that item unless an item with the same SKU already exists,
     * in which case a [DuplicateItemException] is thrown.
     */
    fun addStockItem(sku: String, unitPrice: Int): StockItem {
        if (stockItemRepository.findBySku(sku) != null) {
            throw DuplicateItemException("A stock item already exists with sku $sku")
        }
        return stockItemRepository.save(
            StockItem(
                sku = sku,
                unitPrice = unitPrice
            )
        )
    }

    /**
     * Create and store a new [SpecialOffer] for the [StockItem] identified by the passed in SKU, returning that offer
     * unless an offer already exists for the same [StockItem] in which case a [DuplicateItemException] is thrown, or
     * there is no [StockItem] with that SKU, in which case a [MissingItemException] is thrown.
     */
    fun addSpecialOffer(stockItemSku: String, bundleQuantity: Int, bundlePrice: Int): SpecialOffer {
        if (specialOfferRepository.findOfferForStockItem(stockItemSku) != null) {
            throw DuplicateItemException("A special offer already exists for the stock item with sku $stockItemSku")
        }
        stockItemRepository.findBySku(stockItemSku)?.let {
            return specialOfferRepository.save(
                SpecialOffer(
                    stockItem = it,
                    bundleQuantity = bundleQuantity,
                    bundlePrice = bundlePrice
                )
            )
        } ?: throw MissingItemException("No stock items exist with sku $stockItemSku")

    }

    @PostConstruct
    fun populateInitialStock() {
        addStockItem("A", 50)
        addStockItem("B", 30)
        addStockItem("C", 20)
        addStockItem("D", 15)
        addSpecialOffer("A", 3, 130)
        addSpecialOffer("B", 2, 45)
    }

    
}