package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.checkerframework.checker.units.qual.C

class TestModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        logger.info("Test#enable()")
        commandManager.registerCommand(ClockCommand(plugin))
    }

    override fun onDisable() {
        logger.info("Test#disable()")
    }

    init {
        logger.info("Test#init")
    }
}
