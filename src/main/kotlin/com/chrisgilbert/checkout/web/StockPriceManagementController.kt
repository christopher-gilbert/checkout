package com.chrisgilbert.checkout.web

import com.chrisgilbert.checkout.domain.SpecialOffer
import com.chrisgilbert.checkout.domain.StockItem
import com.chrisgilbert.checkout.exception.DuplicateItemException
import com.chrisgilbert.checkout.service.StockPriceManagementService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"] )
@RestController
class StockPriceManagementController(
    private val stockPriceManagementService: StockPriceManagementService
) {

    @PostMapping("/stockItems")
    fun addItem(@RequestBody item: StockItem): StockItem = stockPriceManagementService.addStockItem(item.sku, item.unitPrice)

    @PostMapping("/stockItems/{itemSku}/offers")
    fun addOffer(@PathVariable itemSku: String, @RequestBody offer: SpecialOffer) = stockPriceManagementService.addSpecialOffer(itemSku, offer.bundleQuantity, offer.bundlePrice)

    @GetMapping("/stockItems")
    fun getItems() = stockPriceManagementService.getStockItems()
}

@ControllerAdvice
class ErrorHandler {

    @ResponseBody
    @ExceptionHandler(DuplicateItemException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun dupicateItem(exception: DuplicateItemException) = exception.message

}