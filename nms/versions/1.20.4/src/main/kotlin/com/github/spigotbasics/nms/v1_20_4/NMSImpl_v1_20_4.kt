package com.github.spigotbasics.nms.v1_20_4

import com.github.spigotbasics.nms.NMSFacade
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView
import java.lang.IllegalStateException

class NMSImpl_v1_20_4 : NMSFacade {
    override fun getTps(): DoubleArray {
        @Suppress("DEPRECATION")
        return (Bukkit.getServer() as CraftServer).handle.server.recentTps
    }

    override fun openWorkbench(
        entity: HumanEntity,
        type: InventoryType,
    ): InventoryView? {
        if (entity !is Player) {
            throw IllegalStateException("this player does not have a connection so no packets could be sent")
        }

        val serverPlayer = (entity as CraftPlayer).handle

        val resultMenu =
            MenuBuilderImpl.build(serverPlayer, type)
                ?: throw IllegalArgumentException("The given inventory type can not be used to create a workbench")
        resultMenu.title = MenuBuilderImpl.title(type)
            ?: throw IllegalArgumentException("the given inventory title was not found with the specified workbench")
        resultMenu.checkReachable = false

        // Now that menu setup is done we will call the intended event in a much less naive manner as to support components
        val event = InventoryOpenEvent(resultMenu.bukkitView)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return null
        }

        val menuType = resultMenu.type
        serverPlayer.connection.send(ClientboundOpenScreenPacket(resultMenu.containerId, menuType, resultMenu.title))
        serverPlayer.containerMenu = resultMenu
        serverPlayer.initMenu(resultMenu)
        return resultMenu.bukkitView
    }
}
