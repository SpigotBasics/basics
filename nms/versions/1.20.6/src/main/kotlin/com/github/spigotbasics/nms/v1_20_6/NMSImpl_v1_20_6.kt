package com.github.spigotbasics.nms.v1_20_6

import com.github.spigotbasics.nms.NMSFacade
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView
import java.lang.IllegalStateException

class NMSImpl_v1_20_6 : NMSFacade {
    override fun getTps(): DoubleArray {
        val serverHandle = (Bukkit.getServer() as CraftServer).handle.server
        return doubleArrayOf(serverHandle.tps1.average, serverHandle.tps5.average, serverHandle.tps15.average)
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
