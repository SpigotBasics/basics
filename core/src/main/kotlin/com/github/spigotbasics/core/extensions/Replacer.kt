package com.github.spigotbasics.core.extensions

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

val mini: MiniMessage = MiniMessage.builder().strict(false).build()
val legacy: LegacyComponentSerializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build()

fun hasPapi(): Boolean {
    return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")
}

/**
 * Applies the given placeholders to the string. Placeholders are in the format %placeholder_name%.
 *
 * @param placeholders The placeholders to apply
 * @return The string with the placeholders applied
 */
fun String.placeholders(placeholders: Map<String, String>): String {
    var result = this

    placeholders.forEach { (placeholder, replacement) ->
        result = result.replace("%${placeholder}%", replacement)
    }
    return result
}


/**
 * Applies basic player Placeholders to the string. This is used as a fallback if PlaceholderAPI is not installed.
 * The following placeholders are available:
 * - player_name
 * - player_uuid
 * - player_world
 * - player_x
 * - player_y
 * - player_z
 * - player_displayname
 *
 * @param player The player to apply the placeholders for
 * @return The string with the placeholders applied
 */
fun String.papiFallbackPlayer(player: Player): String {
    return this.placeholders(mapOf(
        "player_name" to player.name,
        "player_uuid" to player.uniqueId.toString(),
        "player_world" to player.world.name,
        "player_x" to player.location.x.toString(),
        "player_y" to player.location.y.toString(),
        "player_z" to player.location.z.toString(),
        "player_displayname" to player.displayName,
    ))
}


/**
 * Applies PlaceholderAPI. If PlaceholderAPI is not installed, but an online player is given, it falls back to replacing
 * some generic placeholders. See [String.papiFallbackPlayer] for more information.
 *
 * @param player The player to apply the placeholders for
 * @return The string with the placeholders applied
 */
fun String.papi(player: OfflinePlayer? = null): String {
    if(hasPapi()) {
        return PlaceholderAPI.setPlaceholders(player, this)
    } else {
        if(player is Player) {
            return this.papiFallbackPlayer(player)
        } else {
            return this
        }
    }
}

/**
 * Parses MiniMessage into a Component
 *
 * @return The parsed component
 */
fun String.miniComponents(): Component {
    return mini.deserialize(this)
}

fun String.miniComponents(vararg tagResolvers: TagResolver): Component? {
    if(this.isEmpty()) return null
    return mini.deserialize(this, *tagResolvers)
}

/**
 * Parses MiniMessage into a Bukkit String. This is the same as calling [String.miniComponents] and then [Component.toLegacy]
 *
 * @return The parsed string
 */
fun String.miniLegacy(): String {
    return this.miniComponents().toLegacy()
}

/**
 * Parses a Component into a Bukkit String
 *
 * @return The parsed string
 */
fun Component.toLegacy(): String {
    val value = legacy.serialize(this)
    return value
}
