package com.github.spigotbasics.modules.basicsrepair

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class RepairCommand(private val module: BasicsRepairModule)  {


    fun runHandSelf(player: Player) {
        repairHand(player)
        module.msgRepairHandSelf.concerns(player).sendToPlayer(player)
    }


    fun runHandOther(sender: CommandSender, player: Player) {
        repairHand(player)
        module.msgRepairHandOther.concerns(player.player).sendToSender(sender)
    }


    fun runAllSelf(player: Player) {
        repairAll(player)
        module.msgRepairAllSelf.concerns(player).sendToPlayer(player)
    }

    fun runAllOther(sender: CommandSender, player: Player) {
        repairAll(player)
        module.msgRepairAllOther.concerns(player.player).sendToSender(sender)
    }

    private fun repairHand(target: Player) {
        repairItem(target.inventory.itemInMainHand)
    }

    private fun repairAll(target: Player) {
        for (content in target.inventory.contents) {
            repairItem(content)
        }
    }

    private fun repairItem(item: ItemStack?) {
        if (item == null || item.type.isAir) return
        val meta = item.itemMeta
        if(meta is Damageable) {
            if (meta.hasDamage()) {
                meta.damage = 0
                item.setItemMeta(meta)
            }
        }
    }

}
