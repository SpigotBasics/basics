package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.libraries.co.aikar.commands.BaseCommand
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.CommandAlias
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.CommandPermission
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.Description
import com.github.spigotbasics.libraries.co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class RepairCommand(private val module: BasicsRepairModule) : BaseCommand() {

    @CommandAlias("repair|fix")
    @CommandPermission("basics.command.repair.hand")
    @Description("Repairs your currently held item")
    fun runHandSelf(player: Player) {
        repairHand(player)
        module.msgRepairHandSelf.concerns(player).sendToPlayer(player)
    }

    @CommandAlias("repair|fix")
    @CommandPermission("basics.command.repair.hand.other")
    @Description("Repair the given player's currently held item")
    fun runHandOther(sender: CommandSender, player: OnlinePlayer) {
        repairHand(player.player)
        module.msgRepairHandOther.concerns(player.player).sendToSender(sender)
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all")
    @Description("Repair all items in your inventory")
    fun runAllSelf(player: Player) {
        repairAll(player)
        module.msgRepairAllSelf.concerns(player).sendToPlayer(player)
    }

    @CommandAlias("repairall|fixall")
    @CommandPermission("basics.command.repair.all.other")
    @Description("Repair all items in the given player's inventory")
    fun runAllOther(sender: CommandSender, player: OnlinePlayer) {
        repairAll(player.player)
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
