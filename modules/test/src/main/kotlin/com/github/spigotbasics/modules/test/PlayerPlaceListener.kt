package com.github.spigotbasics.modules.test

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class PlayerPlaceListener : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        event.player.sendMessage("You placed a ${event.block.type}")
    }

}
