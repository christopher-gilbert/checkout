package com.chrisgilbert.checkout.exception

/**
 * General exception to indicate the absence of an item that was expected to exist.
 */
class MissingItemException(message: String) : RuntimeException(message)