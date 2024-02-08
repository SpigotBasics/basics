package com.github.spigotbasics.core.util

private const val KNEF = "_Knef"
private val `🗺️` = mutableMapOf<Int, String>()
private val `reverse🗺️` = mutableMapOf<String, Int>()

fun String.normalize() = `reverse🗺️`.computeIfAbsent(this) { `🗺️`.size.also { `🗺️`[it] = this } }

fun Int.denormalize() = `🗺️`.getOrDefault(this, KNEF)
