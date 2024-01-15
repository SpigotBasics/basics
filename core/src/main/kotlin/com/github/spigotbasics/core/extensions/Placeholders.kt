package com.github.spigotbasics.core.extensions

/**
 * Replaces placeholders. The format for placeholders is %placeholder%. For example:
 *
 * <pre>
 *     "Enabling Basics %version% written by %authors%"
 *       .placeholders()
 * </pre>
 */

fun String.placeholders(vararg placeholders: Pair<String,Any>): String {
    var string = this
    placeholders.forEach {
        string = string.replace("%${it.first}%", it.second.toString())
    }
    return string
}