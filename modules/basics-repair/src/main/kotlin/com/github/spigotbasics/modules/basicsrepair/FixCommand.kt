package com.github.spigotbasics.modules.basicsrepair

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player

@CommandAlias("fix")
@CommandPermission("basics.command.repair.hand")
class FixCommand(private val command: RepairCommand) : BaseCommand() {

    @Default
    fun repairHand(player: Player, @CommandPermission("basics.command.repair.hand.other") @Optional other: Player?) {
        command.repairHand(player, other)
    }
}
