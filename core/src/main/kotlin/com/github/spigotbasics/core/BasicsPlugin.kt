package com.github.spigotbasics.core

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleManager
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass

interface BasicsPlugin: Plugin {

    val availableModules: List<KClass<out BasicsModule>>
    val enabledModules: List<BasicsModule>
    val moduleFolder: File
    val moduleManager: ModuleManager
    val commandManager: PaperCommandManager
}
