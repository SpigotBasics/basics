package com.github.spigotbasics.modules.test

import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import java.security.SecureRandom

class PlayerBreakListener {

    private val random: SecureRandom = SecureRandom()

    fun handleBlockBreak(event: BlockBreakEvent) {
        if (event.block.drops.isNotEmpty()) {
            return
        }

        if (random.nextDouble() > 0.5) {
            event.block.world.dropItemNaturally(event.block.location, ItemStack(event.block.type))
        }
    }
}
