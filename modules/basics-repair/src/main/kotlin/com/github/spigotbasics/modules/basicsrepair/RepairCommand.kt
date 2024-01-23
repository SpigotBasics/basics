package com.github.spigotbasics.modules.basicsrepair

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

@CommandAlias("repair|fix")
class RepairCommand(private val module: BasicsRepairModule) : BaseCommand() {

    @Default
    @CommandPermission("basics.command.repair.hand")
    fun runHandSelf(sender: Player) {
        repairSingle(sender, sender)
    }

    @Default
    @CommandPermission("basics.command.repair.hand.other")
    fun runHandOther(sender: CommandSender, target: OnlinePlayer) {
        repairSingle(sender, target.player)
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all")
    fun runAllSelf(sender: Player) {
        repairAll(sender, sender)
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all.other")
    fun runAllOther(sender: CommandSender, target: OnlinePlayer) {
        repairAll(sender, target.player)
    }

    fun repairSingle(sender: CommandSender, target: Player) {
        repairItem(target.inventory.itemInMainHand)

        val message = if (target == sender) {
            module.repairHandSelf
        } else {
            module.repairHand
        }

        message.papi(target).sendMiniTo(module.audience.sender(sender))
    }

    fun repairAll(sender: CommandSender, target: Player) {
        for (content in target.inventory.contents) {
            repairItem(content)
        }

        val message = if (target == sender) {
            module.repairAllSelf
        } else {
            module.repairAll
        }

        message.papi(target).sendMiniTo(module.audience.sender(sender))
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
