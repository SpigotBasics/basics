package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.command.CommandSender

class BukkitRegistryArg<T : Keyed>(name: String, registryClass: Class<T>) : CommandArgument<T>(name) {
    private val registry: Registry<T> = Bukkit.getRegistry(registryClass) ?: error("$registryClass is not a bukkit registry type")

    override fun parse(
        sender: CommandSender,
        value: String,
    ): T? {
        return if (value.isBlank()) null else registry.get(NamespacedKey.fromString(value) ?: return null)
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return registry.map { entry ->
            val namespacedKeyed = entry.key
            if (namespacedKeyed.namespace == NamespacedKey.MINECRAFT) {
                return@map namespacedKeyed.key
            }
            return@map namespacedKeyed.toString()
        }.partialMatches(typing)
    }
}
