package com.github.spigotbasics.core.exceptions

import java.io.IOException

class BasicsStorageAccessException : IOException {
    constructor (message: String, e: Exception) : super(message, e)

    constructor (message: String) : super(message)
}