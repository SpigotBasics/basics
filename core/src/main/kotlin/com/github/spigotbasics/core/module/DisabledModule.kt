package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.event.BasicsEventBus
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.messages.TagResolverFactory
import com.github.spigotbasics.core.module.loader.ModuleJarClassLoader
import com.github.spigotbasics.core.permission.BasicsPermissionManager
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import java.io.File
import java.util.logging.Logger

object DisabledModule : BasicsModule {
    override val plugin: BasicsPlugin
        get() = TODO("Not yet implemented")
    override val moduleClassLoader: ModuleJarClassLoader
        get() = TODO("Not yet implemented")
    override val moduleFile: File
        get() = TODO("Not yet implemented")
    override val info: ModuleInfo
        get() = TODO("Not yet implemented")
    override val config: SavedConfig
        get() = TODO("Not yet implemented")
    override val logger: Logger
        get() = TODO("Not yet implemented")
    override val scheduler: BasicsScheduler
        get() = TODO("Not yet implemented")
    override val messageFactory: MessageFactory
        get() = TODO("Not yet implemented")
    override val commandManager: BasicsCommandManager
        get() = TODO("Not yet implemented")
    override val eventBus: BasicsEventBus
        get() = TODO("Not yet implemented")
    override val tagResolverFactory: TagResolverFactory
        get() = TODO("Not yet implemented")
    override val permissionManager: BasicsPermissionManager
        get() = TODO("Not yet implemented")

    override fun enable(reloadConfig: Boolean) {
        TODO("Not yet implemented")
    }

    override fun disable() {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun reloadConfig() {
        TODO("Not yet implemented")
    }
}