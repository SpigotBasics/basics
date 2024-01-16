package com.github.spigotbasics.modules.test

import cloud.commandframework.Description
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
        registerTestCommand()
    }

    private fun registerTestCommand() {
        val manager = plugin.getCommandManager()
        manager.command(
            manager.commandBuilder("command", Description.of("Test cloud command using a builder"), "alias")
                .handler { context ->
                    context.sender().sendMessage("Cloud sucks so hard, wtf")
                })

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