package com.github.spigotbasics.core.extensions

import org.bukkit.util.StringUtil

fun List<String>.partialMatches(
    string: String,
    completions: MutableList<String> = mutableListOf<String>()
): MutableList<String> = StringUtil.copyPartialMatches(string, this, completions)