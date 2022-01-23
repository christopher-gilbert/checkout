package com.chrisgilbert.checkout

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class CheckoutApplication

fun main(args: Array<String>) {
	runApplication<CheckoutApplication>(*args)
}

