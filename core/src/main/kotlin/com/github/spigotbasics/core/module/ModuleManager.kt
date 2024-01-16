package com.github.spigotbasics.core.module

import java.io.File

interface ModuleManager {

    val loadedModules: List<BasicsModule>

    val enabledModules: List<BasicsModule>

    fun disableModule(module: BasicsModule): Result<Unit>

    fun enableModule(module: BasicsModule): Result<Unit>

    /**
     * Load module
     *
     * @param pluginFile
     * @return
     */
    fun loadModule(pluginFile: File): Result<BasicsModule>

}