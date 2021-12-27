package com.chrisgilbert.checkout.mapper

import com.chrisgilbert.checkout.dto.Sterling
import org.springframework.stereotype.Component

/**
 *
 * Class that takes an Integer quantity of an unspecified currency and presents it as Sterling money
 * (100 pence to the pound).
 */
@Component
class SterlingMapper {

    fun penceToSterling(pence: Int) = Sterling(pence / 100, pence % 100)

}