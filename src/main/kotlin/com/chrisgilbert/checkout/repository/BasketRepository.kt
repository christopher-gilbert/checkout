package com.chrisgilbert.checkout.repository

import com.chrisgilbert.checkout.annotation.VisibleForTesting
import com.chrisgilbert.checkout.domain.Basket
import org.springframework.stereotype.Repository

/**
 * Store of [Baskets][Basket] that are in the checkout process or have completed checkout. Note that any
 * [Baskets][Basket] accessed through the repository are detached from the stored versions, so modifying
 * a returned [Basket] will not affect the stored version - that can only be updated via
 * [BasketRepository#update]
 */
@Repository
@VisibleForTesting
class BasketRepository(
    private val baskets: MutableMap<String, Basket> = mutableMapOf()
) {

    /**
     * Add or replace stored [Basket] based on the passed in [Basket] and return a copy of the stored version.
     */
    fun save(basket: Basket): Basket {
        baskets[basket.id] = (basket.copy())
        return basket.copy()
    }

    /**
     * Return a copy of the stored [Basket] with the passed in ID, or null if it is not found.
     */
    fun findById(basketId: String) = baskets[basketId]?.copy()
}