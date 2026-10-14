package com.github.spigotbasics.core.extensions

fun Double.decimals(decimalPlaces: Int = 0): String {
    return if (decimalPlaces == 0) {
        this.toInt().toString()
    } else {
        "%.${decimalPlaces}f".format(this)
    }
}

fun Float.decimals(decimalPlaces: Int = 0): String {
    return if (decimalPlaces == 0) {
        this.toInt().toString()
    } else {
        "%.${decimalPlaces}f".format(this)
    }
}

/** Formats without a trailing ".0" for whole numbers, e.g. 10.0 -> "10" and 1.5 -> "1.5" */
fun Float.toShortString(): String = toString().removeSuffix(".0")
