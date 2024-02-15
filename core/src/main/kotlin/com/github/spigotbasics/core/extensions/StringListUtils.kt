package com.github.spigotbasics.core.extensions

import org.bukkit.util.StringUtil

fun Iterable<String>.partialMatches(
    string: String,
    completions: MutableList<String> = mutableListOf(),
): List<String> = StringUtil.copyPartialMatches(string, this, completions)

fun List<String>.addAnd(value: String): MutableList<String> {
    val list = if (this is MutableList) this else this.toMutableList()
    list.add(value)
    return list
}

fun List<String>.lastOrEmpty(): String = if (isEmpty()) "" else last()
