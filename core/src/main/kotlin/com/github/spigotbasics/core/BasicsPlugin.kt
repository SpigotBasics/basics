package com.github.spigotbasics.core

import cloud.commandframework.bukkit.BukkitCommandManager
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.logging.Logger
import kotlin.reflect.KClass

interface BasicsPlugin: Plugin {

    val availableModules: List<KClass<out BasicsModule>>
    val enabledModules: List<BasicsModule>
    val moduleFolder: File
    val moduleManager: ModuleManager
    fun getCommandManager(): BukkitCommandManager<CommandSender>

}