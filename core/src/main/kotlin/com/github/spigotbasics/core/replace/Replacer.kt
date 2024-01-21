package com.github.spigotbasics.core.replace

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player

val mini: MiniMessage = MiniMessage.builder().strict(false).build()
val legacy: LegacyComponentSerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build()

fun String.placeholders(vararg placeholders: Pair<*, *>): String {
    var result = this
    placeholders.forEach { (placeholder, replacement) ->
        val placeholderStr = placeholder.toString()
        val replacementStr = replacement.toString()
        result = result.replace("%${placeholderStr}%", replacementStr)
    }
    return result
}

fun String.placeholders(player: Player): String {
    return this.placeholders(
        "player_name" to player.name,
        "player_uuid" to player.uniqueId.toString(),
        "player_world" to player.world.name,
        "player_x" to player.location.x.toString(),
        "player_y" to player.location.y.toString(),
        "player_z" to player.location.z.toString(),
        "player_display_name" to player.displayName,
    )
}

fun String.placeholders(vararg replacements: Any): String {
    var result = this
    replacements.forEach { arg: Any ->
        if(arg is Pair<*,*>) {
            result = result.placeholders(arg)
        } else if(arg is Player) {
            result = result.placeholders(arg)
        }
    }
    return result
}

fun String.miniComponents(): Component {
    return mini.deserialize(this)
}

fun String.mini(): String {
    return this.miniComponents().toLegacy()
}

fun Component.toLegacy(): String {
    return legacy.serialize(this)
}