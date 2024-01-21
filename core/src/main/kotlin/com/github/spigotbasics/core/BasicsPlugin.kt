package com.github.spigotbasics.core

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.module.manager.ModuleManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import java.io.File

interface BasicsPlugin: Plugin {

    //val logger: Logger // Already in Bukkit:Plugin
    val moduleFolder: File
    val moduleManager: ModuleManager
    val audience: BukkitAudiences

    fun createCommandManager(): BasicsCommandManager
}
