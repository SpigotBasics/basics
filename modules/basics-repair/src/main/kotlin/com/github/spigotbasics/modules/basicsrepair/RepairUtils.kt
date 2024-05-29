package com.github.spigotbasics.modules.basicsrepair

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

fun repairHand(target: Player) {
    repairItem(target.inventory.itemInMainHand)
}

fun repairAll(target: Player) {
    for (content in target.inventory.contents) {
        repairItem(content)
    }
}

fun repairItem(item: ItemStack?) {
    if (item == null || item.type.isAir) return
    val meta = item.itemMeta
    if (meta is Damageable) {
        if (meta.hasDamage()) {
            meta.damage = 0
            item.setItemMeta(meta)
        }
    }
}
