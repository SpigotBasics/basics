package com.github.spigotbasics.core.messages.tags

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PlaceholderAPITagFactory {

    val nonPlayerPapi = createNonPlayerPapi()

    private fun createNonPlayerPapi(): TagResolver {
        return TagResolver.resolver("papi") { args, _ ->
            val content = args.popOr("string expected").value()
            Tag.inserting(Component.text().content(replacePlaceholders(content)))
        }

    }

    fun playerPapi(player: Player): TagResolver {
        return TagResolver.resolver("papi") { args, _ ->
            val content = args.popOr("string expected").value()
            Tag.inserting(Component.text().content(replacePlaceholders(content, player)))
        }
    }

    fun replacePlaceholders(message: String, player: OfflinePlayer? = null): String {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, message)
        } else {
            return message
        }
    }

}