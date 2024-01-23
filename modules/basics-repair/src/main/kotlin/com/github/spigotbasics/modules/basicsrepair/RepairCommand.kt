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

    private val audience = module.audience

    @Default
    @CommandPermission("basics.command.repair.hand")
    fun runHandSelf(player: Player) {
        repairHand(player)
        module.msgRepairHandSelf.concerns(player).sendMiniTo(audience.player(player))
    }

    @Default
    @CommandPermission("basics.command.repair.hand.other")
    fun runHandOther(sender: CommandSender, target: OnlinePlayer) {
        repairHand(target.player)
        module.msgRepairHandOther.concerns(target.player).sendMiniTo(audience.sender(sender))
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all")
    fun runAllSelf(player: Player) {
        repairAll(player)
        module.msgRepairAllSelf.concerns(player).sendMiniTo(audience.player(player))
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all.other")
    fun runAllOther(sender: CommandSender, target: OnlinePlayer) {
        repairAll(target.player)
        module.msgRepairAllOther.concerns(target.player).sendMiniTo(audience.sender(sender))
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
