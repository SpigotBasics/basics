package com.github.spigotbasics.core

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.logging.Logger
import kotlin.reflect.KClass

interface BasicsPlugin: Plugin {

    //val logger: Logger // Already in Bukkit:Plugin
    val moduleFolder: File
    val moduleManager: ModuleManager
    val commandManager: PaperCommandManager
}
