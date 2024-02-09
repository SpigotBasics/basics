package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.core.util.TextFormatter
import org.bukkit.util.StringUtil

fun String.startsWithIgnoreCase(prefix: String) = StringUtil.startsWithIgnoreCase(this, prefix)

fun String.toHumanReadable() = TextFormatter.toHumanReadable(this)
