package com.github.spigotbasics.common

import java.util.TreeMap

/**
 * A case-insensitive map.
 *
 * @param T The type of the values in the map.
 * @constructor Create empty Dictionary
 */
class Dictionary<T> : MutableMap<String, T>, TreeMap<String, T>(String.CASE_INSENSITIVE_ORDER)
