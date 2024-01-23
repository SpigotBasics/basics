package com.github.spigotbasics.modules.basicsrepair

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Optional
import org.bukkit.entity.Player

@CommandAlias("fixall")
@CommandPermission("basics.command.repair.all")
class FixAllCommand(private val command: RepairCommand) : BaseCommand() {

    @Default
    fun repairAll(player: Player, @CommandPermission("basics.command.repair.all.other") @Optional other: Player?) {
        command.repairAll(player, other)
    }

}
