package com.github.spigotbasics.core

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.CoreConfigManager
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import com.github.spigotbasics.core.module.manager.ModuleManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.Plugin
import java.io.File

interface BasicsPlugin: Plugin {

    val moduleFolder: File
    val moduleManager: ModuleManager
    val audience: BukkitAudiences
    val tagResolverFactory: TagResolverFactory

    val coreConfigManager: CoreConfigManager

    fun createCommandManager(): BasicsCommandManager

    fun reloadCoreConfig()
}
