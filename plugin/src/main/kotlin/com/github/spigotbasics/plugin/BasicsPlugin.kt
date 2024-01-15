package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.extensions.placeholders
import org.bukkit.plugin.java.JavaPlugin

class BasicsPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info(
            "Basics v%version% enabled! This plugin was written by %authors%."
                .placeholders(
                    "version" to description.version,
                    "authors" to "cool people"
                )
        )
    }
}