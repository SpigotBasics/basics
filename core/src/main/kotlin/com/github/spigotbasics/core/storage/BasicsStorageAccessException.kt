package com.github.spigotbasics.core.storage

import java.io.IOException

class BasicsStorageAccessException(message: String, e: Exception) : IOException(message, e) {
}