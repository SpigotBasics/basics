package com.github.spigotbasics.core.exceptions

import java.io.IOException

class BasicsStorageInitializeException(e: Exception) : IOException("Could not initialize backend storage", e)