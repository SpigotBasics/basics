package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.core.model.RomanNumerals

fun Int.toRoman(): String {
    return RomanNumerals.toRoman(this)
}