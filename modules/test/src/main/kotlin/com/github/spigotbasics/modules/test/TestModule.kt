package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class TestModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        logger.info("Test#enable()")
    }

    override fun onDisable() {
        logger.info("Test#disable()")
    }

    override fun onLoad() {
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
