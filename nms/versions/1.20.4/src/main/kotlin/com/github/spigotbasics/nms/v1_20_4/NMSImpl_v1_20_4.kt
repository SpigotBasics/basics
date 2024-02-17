package com.github.spigotbasics.nms.v1_20_4

import com.github.spigotbasics.nms.NMSFacade
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView
import java.lang.IllegalStateException

class NMSImpl_v1_20_4 : NMSFacade {
    override fun getTps(): DoubleArray {
        @Suppress("DEPRECATION") return (Bukkit.getServer() as CraftServer).handle.server.recentTps
    }

    override fun openWorkbench(entity: HumanEntity, type: InventoryType): InventoryView {
        if (entity !is Player) {
            throw IllegalStateException("this player does not have a connection so no packets could be sent")
        }

        val serverPlayer = (entity as CraftPlayer).handle

        val menu = MenuBuilderImpl.build(serverPlayer, type)
            ?: throw IllegalArgumentException("The given inventory type can not be used to create a workbench")

        entity.openInventory(menu.bukkitView)
        return menu.bukkitView
    }
}
