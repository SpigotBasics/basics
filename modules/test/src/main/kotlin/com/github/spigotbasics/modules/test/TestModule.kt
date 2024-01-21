package com.github.spigotbasics.modules.test

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.checkerframework.checker.units.qual.C

class TestModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        logger.info("Test#enable()")
        commandManager.registerCommand(ClockCommand(this))
        eventBus.subscribe(BlockBreakEvent::class.java) { event ->
            event.player.sendMessage("you broke a ${event.block.type.name}")
        }
        eventBus.subscribe(BlockBreakEvent::class.java, PlayerBreakListener()::handleBlockBreak)
        eventBus.subscribe(PlayerPlaceListener())
    }

    override fun onDisable() {
        logger.info("Test#disable()")
    }

    init {
        logger.info("Test#init")
    }

}
