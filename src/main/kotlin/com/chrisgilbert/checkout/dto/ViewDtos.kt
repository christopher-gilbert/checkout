package com.chrisgilbert.checkout.dto

import com.chrisgilbert.checkout.domain.Basket

/**
 * Read only view of basket contents for presentation. Actually a read only view is not essential as
 * there are no controller functions that bind directly to a [Basket] (and would hence allow uncontrolled
 * setting of basket properties).
 */
data class BasketDto(
    val basketId: String,
    val items: List<StockItemDto>
)

data class StockItemDto(
    val sku: String,
    val quantity: Int? = 1,
)

/**
 * Representation of UK money.
 */
data class Sterling(
    val pounds: Int,
    val pence: Int
)