package com.github.spigotbasics.pipe.paper

import com.github.spigotbasics.pipe.OpenInventoryFacade
import org.bukkit.entity.Player

object PaperOpenInventoryFacade : OpenInventoryFacade {
    override fun openCartographyTable(player: Player) {
        player.openCartographyTable(null, true)
    }

    override fun openLoom(player: Player) {
        player.openLoom(null, true)
    }

    override fun openGrindstone(player: Player) {
        player.openGrindstone(null, true)
    }

    override fun openSmithingTable(player: Player) {
        player.openSmithingTable(null, true)
    }

    override fun openStonecutter(player: Player) {
        player.openStonecutter(null, true)
    }

    override fun openAnvil(player: Player) {
        player.openAnvil(null, true)
    }
}