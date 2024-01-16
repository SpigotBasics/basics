package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import java.util.logging.Logger

abstract class AbstractBasicsModule(protected val plugin: BasicsPlugin, final override val info: ModuleInfo) : BasicsModule {

    override val logger: Logger = Logger.getLogger(info.mainClass)

    // Loading the config should happen here

    override fun enable() { }

    override fun disable() { }

    override fun load() { }



}