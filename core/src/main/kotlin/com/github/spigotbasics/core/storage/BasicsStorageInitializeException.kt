package com.github.spigotbasics.core.storage

import java.io.IOException

class BasicsStorageInitializeException(e: Exception) : IOException("Could not inizialize backend storage", e) {

}