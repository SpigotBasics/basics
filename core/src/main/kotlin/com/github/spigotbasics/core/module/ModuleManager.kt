package com.github.spigotbasics.core.module

import java.io.File
import com.github.spigotbasics.core.EitherResult

interface ModuleManager {

    val loadedModules: List<BasicsModule>

    val enabledModules: List<BasicsModule>

    fun disableModule(module: BasicsModule)

    fun enableModule(module: BasicsModule)

    /**
     * Load module
     *
     * @param pluginFile
     * @return
     */
    fun loadModule(pluginFile: File): EitherResult<BasicsModule, Exception>

}