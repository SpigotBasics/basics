package com.github.spigotbasics.core.extensions

fun String.genitiveSuffix(): String =
    when (lowercase().last()) {
        's', 'x', 'z' -> "'"
        else -> "'s"
    }
