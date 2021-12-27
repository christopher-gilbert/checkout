package com.chrisgilbert.checkout.exception

/**
 * General exception to indicate an attempt to create a new item that already exists.
 */
class DuplicateItemException(message: String) : RuntimeException(message)