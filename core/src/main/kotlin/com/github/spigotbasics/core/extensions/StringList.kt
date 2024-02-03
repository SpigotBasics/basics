package com.github.spigotbasics.core.extensions

import org.bukkit.util.StringUtil

fun List<String>.partialMatches(
    string: String,
    completions: MutableList<String> = mutableListOf()
): MutableList<String> = StringUtil.copyPartialMatches(string, this, completions)

fun MutableList<String>.addAnd(value: String): MutableList<String> {
    add(value)
    return this
}