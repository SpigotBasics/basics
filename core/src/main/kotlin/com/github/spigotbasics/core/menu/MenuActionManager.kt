package com.github.spigotbasics.core.menu

import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

class MenuActionManager(module: BasicsModule) : Listener {

    private val registry: MutableMap<Inventory, MenuActionHandler> = HashMap()

    init {
        module.eventBus.subscribe(InventoryOpenEvent::class.java, this::handleOpen)
        module.eventBus.subscribe(InventoryCloseEvent::class.java, this::handleClose)
        module.eventBus.subscribe(InventoryClickEvent::class.java, this::handleClick)
    }

    fun register(actionHandler: MenuActionHandler) {
        this.registry[actionHandler.inventory] = actionHandler
    }

    fun unregister(inventory: Inventory) {
        this.registry.remove(inventory)
    }

    fun clear() {
        registry.clear()
    }

    private fun handleOpen(event: InventoryOpenEvent) {
        val actionHandler: MenuActionHandler = registry[event.inventory] ?: return
        actionHandler.handleOpen(event)
    }

    private fun handleClose(event: InventoryCloseEvent) {
        val actionHandler: MenuActionHandler = registry[event.inventory] ?: return
        actionHandler.handleClose(event)
    }

    private fun handleClick(event: InventoryClickEvent) {
        val actionHandler: MenuActionHandler = registry[event.inventory] ?: return
        actionHandler.handleClick(event)
    }
}
