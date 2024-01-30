package com.github.spigotbasics.core.extensions

import org.bukkit.util.StringUtil

fun String.startsWithIgnoreCase(prefix: String) = StringUtil.startsWithIgnoreCase(this, prefix)

fun String.asUUIDKey() {
    val bytes = this.toByteArray()
}