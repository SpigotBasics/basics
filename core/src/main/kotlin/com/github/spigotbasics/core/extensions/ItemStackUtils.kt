package com.github.spigotbasics.core.extensions

import org.bukkit.inventory.ItemStack

fun ItemStack.toSnbtWithType(): String {
    val material = type.name.lowercase()
    val meta = itemMeta
    val metaString = meta?.asString ?: ""
    return "minecraft:$material$metaString"
}

fun ItemStack.toSnbtWithoutType(): String {
    val meta = itemMeta
    val metaString = meta?.asString ?: ""
    return metaString
}
