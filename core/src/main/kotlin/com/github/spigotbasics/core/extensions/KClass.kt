package com.github.spigotbasics.core.extensions

import java.io.InputStream
import java.net.URL
import kotlin.reflect.KClass

fun KClass<*>.getCustomResource(path: String): URL? {
    return java.getCustomResource(path)
}

fun Class<*>.getCustomResource(path: String): URL? {
    val actualPath = toAbsoluteResourcePath(path)
    return getResource(actualPath)
}

fun KClass<*>.getCustomResourceAsStream(path: String): InputStream? {
    return java.getCustomResourceAsStream(path)
}

fun Class<*>.getCustomResourceAsStream(path: String): InputStream? {
    val actualPath = toAbsoluteResourcePath(path)
    return getResourceAsStream(actualPath)
}

private fun toAbsoluteResourcePath(path: String): String {
    return if (path.substring(0, 1) == "/") path else "/$path"
}