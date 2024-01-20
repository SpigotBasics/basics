package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInfo

class TestModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override fun enable() {
        logger.info("Test#enable()")
    }

    override fun disable() {
        logger.info("Test#disable()")
        scheduler.killAll()
    }

    override fun load() {
        logger.info("Test#load()")

        val testResourceContent = getResource("/test.txt")!!.readText()
        logger.info("config.yml foo: ${config.getString("foo")}")
        logger.info("config.yml bar: ${config.getString("bar")}")
        logger.info("Test resource content: $testResourceContent")

    }

    init {
        logger.info("Test#init")
    }
}
