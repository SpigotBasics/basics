package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.extensions.placeholders

abstract class AbstractBasicsModule(protected val plugin: BasicsPlugin) : BasicsModule {

    // Loading the config should happen here

    override fun enable() {
        plugin.logger.info("Enabling module %name%...".placeholders(
            "name" to this.name
        ))
    }

}