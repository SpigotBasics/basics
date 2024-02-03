package com.github.spigotbasics.core.messages

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin

class AudienceProvider(private val plugin: Plugin) {
    val audience: BukkitAudiences by lazy { BukkitAudiences.create(plugin) }
}