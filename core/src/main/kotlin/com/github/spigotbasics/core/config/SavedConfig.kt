package com.github.spigotbasics.core.config

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class SavedConfig(private val saveFile: File) : YamlConfiguration() {

    fun save() {
        save(saveFile)
    }

}
