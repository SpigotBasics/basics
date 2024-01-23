package com.github.spigotbasics.modules.basicsrepair

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Flags
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Subcommand
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

@CommandAlias("repair")
@CommandPermission("basics.command.repair")
class RepairCommand(private val module: BasicsRepairModule) : BaseCommand() {

    @Subcommand("hand")
    @CommandPermission("basics.command.repair.hand")
    fun repairHand(player: Player, @CommandPermission("basics.command.repair.hand.other") @Optional other: Player?) {
        val target = other ?: player

        repairItem(target.inventory.itemInMainHand)
        if (target == player) {
            module.repairHandSelf.papi(target).sendMiniTo(module.audience.player(player))
        } else {
            module.repairHand.papi(target).sendMiniTo(module.audience.player(player))
        }
    }

    @Subcommand("all")
    @CommandPermission("basics.command.repair.all")
    fun repairAll(player: Player, @CommandPermission("basics.command.repair.all.other") @Optional other: Player?) {
        val target = other ?: player

        for (content in target.inventory.contents) {
            repairItem(content)
        }

        if (target == player) {
            module.repairAllSelf.papi(target).sendMiniTo(module.audience.player(player))
        } else {
            module.repairAll.papi(target).sendMiniTo(module.audience.player(player))
        }
    }

    fun repairItem(item: ItemStack?): Boolean {
        if (item == null) return false
        val meta = item.itemMeta as Damageable
        if (meta.hasDamage()) {
            meta.damage = 0
            item.setItemMeta(meta)
        }
        return true
    }

}
