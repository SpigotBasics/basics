package com.github.spigotbasics.core.config

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class SavedConfig(private val saveFile: File) : YamlConfiguration() {

    fun save() {
        save(saveFile)
    }

    companion object {
        fun saveAndLoadDefaults(saveFile: File, resourceStream: InputStream): SavedConfig {
            val configuration = SavedConfig(saveFile);
            configuration.setDefaults(YamlConfiguration.loadConfiguration(saveFile));

            if (!saveFile.exists()) {
                saveFile.createNewFile();
                resourceStream.copyTo(FileOutputStream(saveFile))
            }

            configuration.load(saveFile)
            return configuration;
        }
    }
}
