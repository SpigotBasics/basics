package com.github.spigotbasics.core.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

interface MenuActionHandler {

    val inventory: Inventory

    fun open(player: Player)
    fun close()

    fun handleClick(event: InventoryClickEvent)
    fun handleOpen(event: InventoryOpenEvent)
    fun handleClose(event: InventoryCloseEvent)
}
