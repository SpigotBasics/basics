package com.github.spigotbasics.core.util

object TextFormatter {
    fun toHumanReadable(value: String): String {
        return value.split("_").joinToString(" ") { word -> word.lowercase().replaceFirstChar(Char::uppercase) }
    }
}
