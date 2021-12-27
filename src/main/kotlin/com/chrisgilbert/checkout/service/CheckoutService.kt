package com.chrisgilbert.checkout.service

import com.chrisgilbert.checkout.domain.Basket
import com.chrisgilbert.checkout.domain.PricingRules
import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import com.chrisgilbert.checkout.exception.MissingItemException
import com.chrisgilbert.checkout.repository.BasketRepository
import com.chrisgilbert.checkout.repository.SpecialOfferRepository
import com.chrisgilbert.checkout.repository.StockItemRepository
import org.springframework.stereotype.Service

/**
 * Main entry point to the checkout process, allowing a [Basket] of [StockItems][StockItem] to be created,
 * added to, and priced.
 */
@Service
class CheckoutService(
    private val stockItemRepository: StockItemRepository,
    private val specialOfferRepository: SpecialOfferRepository,
    private val basketRepository: BasketRepository
) {

    /**
     * Create a new [Basket] associated with the set of [pricing rules][PricingRules]
     * based on [SpecialOffers][SpecialOffer] have been previously stored. These rules will be
     * valid for the lifetime of the [Basket], even if the stored rules are updated.
     */
    fun startCheckout() = basketRepository.save(Basket(applicablePricingRules = getCurrentPricingRules()))

    /**
     * Add the [StockItem] identified by the SKU to the [Basket] with the passed in basketId and return the updated
     * basket, if the basketId and SKU exist, else throw an exception.
     */
    fun addItem(sku: String, basketId: String) =
        basketRepository.findById(basketId)
            ?.addItem(requireNotNull(stockItemRepository.findBySku(sku)))
            ?.let { basketRepository.save(it) }
            ?: throw MissingItemException("basket with ID $basketId does not exist")


    /**
     * Calculate a total price for all the [StockItems][StockItem] in the [Basket] in pence, accounting
     * for any [SpecialOffers][SpecialOffer] that are applicable to the items. Prices and offers are the
     * ones associated with the basket at the start of checkout.
     */
    fun calculateTotalPrice(basketId: String): Int {
        val basket = basketRepository.findById(basketId)
            ?: throw MissingItemException("basket with ID $basketId does not exist")
        return basket
            .getSummary()
            .map { (item, quantity) -> basket.getPricingStrategy(item).priceOf(quantity) }
            .sum()
    }

    private fun getCurrentPricingRules() =
        PricingRules(specialOfferRepository.getCurrentSpecialOffers())

}