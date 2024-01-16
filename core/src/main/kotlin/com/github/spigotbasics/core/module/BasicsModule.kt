package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.config.BasicsConfig
import java.util.logging.Logger

/**
 * Common interfaces to be used by all modules. Implementations require a public constructor that takes in a [BasicsPlugin] and a [ModuleInfo].
 *
 * @constructor Create empty Basics plugin
 */
interface BasicsModule {

    /**
     * Info about this module
     */
    val info: ModuleInfo

    /**
     * This module's config
     */
    val config: BasicsConfig

    val logger: Logger

    /**
     * Called when the module is enabled
     *
     */
    fun enable()

    /**
     * Called when the module is disabled
     *
     */
    fun disable()

    /**
     * Called when the module is loaded
     *
     */
    fun load()

}