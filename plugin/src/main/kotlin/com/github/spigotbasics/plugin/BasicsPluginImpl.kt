package com.github.spigotbasics.plugin

import co.aikar.commands.PaperCommandManager
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.Bukkit.getScheduler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.util.function.Consumer
import kotlin.reflect.KClass

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {
    override val availableModules: MutableList<KClass<out BasicsModule>> = ArrayList()
    override val enabledModules: MutableList<BasicsModule> = ArrayList()
    override val moduleFolder = File(dataFolder, "modules")
    override val moduleManager = ModuleManager(moduleFolder)
    override val commandManager: PaperCommandManager by lazy {
        PaperCommandManager(this)
    }

    override fun onLoad() {
        if (!moduleFolder.isDirectory) {
            logger.info("Creating modules folder at ${moduleFolder.absolutePath}")
            moduleFolder.mkdirs()
        }
    }

    override fun onEnable() {
        commandManager.registerCommand(BasicsCommand());


    }

}
