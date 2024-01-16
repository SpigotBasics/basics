package com.github.spigotbasics.core.module

import java.io.FileFilter
import kotlin.reflect.KClass

/**
 * Module loader
 *
 * @constructor Create empty Module loader
 */
interface ModuleLoader {

    companion object {
        val jarFileFilter = FileFilter {
            it.isFile && it.name.lowercase().endsWith(".jar")
        }
        const val MODULE_INFO_FILE_NAME = "basics-module.yml"
    }

    fun loadModuleInfo(): ModuleInfo

    fun loadMainClass(): KClass<out BasicsModule>
}