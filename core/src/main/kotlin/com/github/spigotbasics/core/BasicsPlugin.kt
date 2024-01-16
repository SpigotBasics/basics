package com.github.spigotbasics.core

import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.plugin.Plugin
import java.io.File
import kotlin.reflect.KClass

interface BasicsPlugin: Plugin {

    val availableModules: List<KClass<out BasicsModule>>
    val enabledModules: List<BasicsModule>
    val moduleFolder: File

    fun loadModule(clazz: KClass<out BasicsModule>): Result<BasicsModule, Exception>

}