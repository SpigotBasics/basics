package com.github.spigotbasics.core

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.plugin.Plugin
import java.io.File

interface BasicsPlugin: Plugin {

    //val logger: Logger // Already in Bukkit:Plugin
    val moduleFolder: File
    val moduleManager: ModuleManager

    fun createCommandManager(): BasicsCommandManager
}
