package com.github.spigotbasics.nms

import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView
import kotlin.jvm.Throws

interface NMSFacade {
    fun getTps(): DoubleArray

    /**
     * Opens a workbench of the provided type, if supported and returns the InventoryView
     *
     * @param entity the human entity opening the workbench
     * @param type the type of workbench being open
     * @return the InventoryView
     * @throws IllegalArgumentException if the given type is not supported
     * @throws IllegalStateException if the given entity is not a player with a connection
     */
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun openWorkbench(entity: HumanEntity, type: InventoryType): InventoryView
}
