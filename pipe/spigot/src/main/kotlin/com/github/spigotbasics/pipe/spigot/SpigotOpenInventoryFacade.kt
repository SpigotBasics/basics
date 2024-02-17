package com.github.spigotbasics.pipe.spigot

import com.github.spigotbasics.nms.NMSFacade
import com.github.spigotbasics.pipe.OpenInventoryFacade
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType

class SpigotOpenInventoryFacade(private val nms: NMSFacade) : OpenInventoryFacade {
    override fun openAnvil(player: Player) {
        nms.openWorkbench(player, InventoryType.ANVIL)
    }

    override fun openCartographyTable(player: Player) {
        nms.openWorkbench(player, InventoryType.CARTOGRAPHY)
    }

    override fun openGrindstone(player: Player) {
        nms.openWorkbench(player, InventoryType.GRINDSTONE)
    }

    override fun openLoom(player: Player) {
        nms.openWorkbench(player, InventoryType.LOOM)
    }

    override fun openSmithingTable(player: Player) {
        nms.openWorkbench(player, InventoryType.SMITHING)
    }

    override fun openStonecutter(player: Player) {
        nms.openWorkbench(player, InventoryType.STONECUTTER)
    }
}
