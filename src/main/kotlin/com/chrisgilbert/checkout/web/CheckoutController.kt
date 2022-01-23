package com.chrisgilbert.checkout.web

import com.chrisgilbert.checkout.dto.BasketDto
import com.chrisgilbert.checkout.dto.StockItemDto
import com.chrisgilbert.checkout.mapper.BasketMapper
import com.chrisgilbert.checkout.mapper.SterlingMapper
import com.chrisgilbert.checkout.service.CheckoutService
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.*

@RestController
class CheckoutController(
    private val checkoutService: CheckoutService,
    private val basketMapper: BasketMapper,
    private val sterlingMapper: SterlingMapper
) {

    /**
     * Start checkout process by creating a new empty basket.
     */
    @GetMapping("/baskets")
    fun createBasket() = basketMapper
        .basketToBasketDto(checkoutService.startCheckout())

    /**
     * Add a single item to the stored version of the basket, and return an
     * updated view. Note that this API does not follow REST conventions - to do so would
     * require a PATCH or PUT of a basket, and patching an additional item in would really
     * require the API to expose the list of distinct items in the basket leaving the client
     * to form a presentable view of that raw data.
     */
    @PatchMapping("/baskets/{basketId}")
    fun scanItem(@RequestBody item: StockItemDto, @PathVariable basketId: String): BasketDto {
        val basket = checkoutService.addItem(item.sku, basketId)
        return basketMapper.basketToBasketDto(basket)
    }

    @GetMapping("/baskets/{basketId}/total")
    fun getTotalPrice(@PathVariable basketId: String) = sterlingMapper.penceToSterling(checkoutService.calculateTotalPrice(basketId))

    /**
     * checking the behaviour when caffiene is configured but not on the classpath
     */
    @Cacheable("web-cache")
    @GetMapping("/hello")
    fun getAValue(): String {
        return "hello there"
    }
}