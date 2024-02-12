package com.github.spigotbasics.common

import java.util.TreeMap

/**
 * A case-insensitive map.
 *
 * @param T The type of the values in the map.
 * @constructor Create empty Dictionary
 */
class Dictionary<T> : MutableMap<String, T>, TreeMap<String, T>(String.CASE_INSENSITIVE_ORDER) {
    companion object {
        fun fromEnum(
            enumClass: Class<out Enum<*>>,
            lowercase: Boolean = false,
        ): Dictionary<Enum<*>> {
            val dictionary = Dictionary<Enum<*>>()
            for (enumConstant in enumClass.enumConstants) {
                val name = if (lowercase) enumConstant.name.lowercase() else enumConstant.name
                dictionary[name] = enumConstant
            }
            return dictionary
        }

        fun <T> from(entries: List<Pair<String, T>>): Dictionary<T> {
            val dictionary = Dictionary<T>()
            for ((key, value) in entries) {
                dictionary[key] = value
            }
            return dictionary
        }
    }
}
