package com.github.spigotbasics.nms

import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.InventoryView

class UnsupportedNMSFacade : NMSFacade {
    override fun getTps(): DoubleArray {
        throw NMSNotSupportedException()
    }

    override fun openWorkbench(
        entity: HumanEntity,
        type: InventoryType,
    ): InventoryView {
        throw NMSNotSupportedException()
    }
}
