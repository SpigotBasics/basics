package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.BasicsConfig
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.BasicsModule

class TestModule(plugin: BasicsPlugin) : AbstractBasicsModule(plugin) {
    override val name = "test"
    override val description = "Useless test module"
    override val version = "0.1" // TODO: Move this to AbstractBasicsModule and get it from resources
    override val config = BasicsConfig() // TODO: Move this to AbstractBasicsModule

    override fun enable() {
        super.enable()
        plugin.logger.info("THIS IS ENABLE METHOD ON TEST MODULE")
    }
}