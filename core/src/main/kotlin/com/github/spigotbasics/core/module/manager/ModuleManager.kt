package com.github.spigotbasics.core.module.manager

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.InvalidModuleException
import com.github.spigotbasics.core.module.loader.ModuleJarFileFilter
import com.github.spigotbasics.core.module.loader.ModuleLoader
import java.io.File
import java.io.FileNotFoundException

class ModuleManager(val modulesDirectory: File) {

    private val modules: Map<File, BasicsModule> = mutableMapOf()

    init {
        if (!modulesDirectory.isDirectory()) {
            modulesDirectory.mkdirs()
        }
    }


    fun findModules(directory: File): List<ModuleLoader> {
        val modules = mutableListOf<ModuleLoader>()
        val moduleFiles = directory.listFiles(ModuleJarFileFilter)
            ?: throw FileNotFoundException("Directory $directory does not exist or cannot be accessed")

        moduleFiles.forEach {
            try {
                modules.add(ModuleLoader(it))
            } catch (e: InvalidModuleException) {
                e.printStackTrace()
            }
        }
        return modules
    }


}