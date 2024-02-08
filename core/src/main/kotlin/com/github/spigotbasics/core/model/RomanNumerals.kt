package com.github.spigotbasics.core.model

object RomanNumerals {
    fun toRoman(value: Int): String {
        return when (value) {
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            10 -> "X"
            else -> return value.toString()
        }
    }
}
