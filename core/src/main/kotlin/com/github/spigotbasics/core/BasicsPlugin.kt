package com.github.spigotbasics.core

import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

interface BasicsPlugin: Plugin {

    val availableModules: List<KClass<out BasicsModule>>
    val enabledModules: List<BasicsModule>

    fun loadModule(clazz: KClass<out BasicsModule>): Either<BasicsModule, Exception>

}