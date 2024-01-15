package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.config.BasicsConfig

interface BasicsModule {

    /**
     * The module's name, e.g. "tpa"
     */
    val name: String

    /**
     * Short description of the module's features
     */
    val description: String

    /**
     * Module version
     */
    val version: String

    /**
     * This module's config
     */
    val config: BasicsConfig


}