package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.BasicsConfig
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import java.util.logging.Logger

class TestModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override val config = BasicsConfig() // TODO: Move this to AbstractBasicsModule

    override fun enable() {
        logger.info("Test#enable()")
    }

    override fun disable() {
        logger.info("Test#disable()")
    }

    override fun load() {
        logger.info("Test#load()")

        val testResourceContent = getResource("/test.txt").readText()
        logger.info("Test resource content: $testResourceContent")
    }

    init {
        logger.info("Test#init")
    }
}