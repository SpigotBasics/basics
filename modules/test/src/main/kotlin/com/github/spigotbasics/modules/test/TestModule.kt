package com.github.spigotbasics.modules.test

import cloud.commandframework.Description
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInfo

class TestModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override fun enable() {
        logger.info("Test#enable()")
        registerTestCommand()
    }

    private fun registerTestCommand() {
        commandManager.command(
            commandManager.commandBuilder("basicstest", Description.of("Basics test command"))
                .handler { context ->
                    context.sender().sendMessage("Basics Test module is running fine!")
                })

    }

    override fun disable() {
        logger.info("Test#disable()")
    }

    override fun load() {
        logger.info("Test#load()")

        val testResourceContent = getResource("test.txt").readText()
        logger.info("Test config content:" + config.get("foo"))
        logger.info("Test resource content: $testResourceContent")
    }

    init {
        logger.info("Test#init")
    }
}
