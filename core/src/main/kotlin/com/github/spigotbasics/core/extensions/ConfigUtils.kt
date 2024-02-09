package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.core.util.DurationParser
import org.bukkit.configuration.MemorySection

fun MemorySection.getAsNewLineSeparatedString(key: String): String {
    val value = get(key) ?: return ""
    if (value is List<*>) {
        return value.joinToString("\n")
    }
    return value.toString()
}

fun MemorySection.getDurationAsTicks(
    key: String,
    defaultValue: Long,
): Long {
    return DurationParser.parseDurationToTicks(getString(key) ?: return defaultValue)
}

fun MemorySection.getDurationAsMillis(
    key: String,
    defaultValue: Long,
): Long {
    return DurationParser.parseDurationToMillis(getString(key) ?: return defaultValue)
}

/**
 * Get a list of strings, no matter if the value is a list or a single string.
 *
 * @param key Key
 * @return List of strings
 */
fun MemorySection.getAsStringList(key: String): List<String> {
    val value = get(key) ?: return emptyList()
    if (value is List<*>) {
        return value.map { it.toString() }
    }
    if (value is String) {
        return value.split("\n")
    }
    return listOf(value.toString())
}
