package com.chrisgilbert.checkout.mapper

import com.chrisgilbert.checkout.domain.Basket
import com.chrisgilbert.checkout.dto.BasketDto
import com.chrisgilbert.checkout.dto.StockItemDto
import org.springframework.stereotype.Component

/**
 * Transform a [Basket] domain object into a separate immutable view to allow controlled access to
 * the properties of a [Basket].
 */
@Component
class BasketMapper {

    fun basketToBasketDto(basket: Basket) =
        BasketDto(
            basketId = basket.id,
            items = basket.getSummary()
                .map { (item, quantity) -> StockItemDto(item.sku, quantity) }
        )

}
