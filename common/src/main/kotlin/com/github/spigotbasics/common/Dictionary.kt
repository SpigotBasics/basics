package com.github.spigotbasics.common

import java.util.TreeMap

class Dictionary<T> : Map<String, T>, TreeMap<String, T>(String.CASE_INSENSITIVE_ORDER) {
    companion object {
        inline fun <reified E : Enum<E>> fromEnumClass(lowercase: Boolean = false): Dictionary<E> {
            val dictionary = Dictionary<E>()
            for (enumConstant in enumValues<E>()) {
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
