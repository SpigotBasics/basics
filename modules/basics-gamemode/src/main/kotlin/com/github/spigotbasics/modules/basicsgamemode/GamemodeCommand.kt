package com.github.spigotbasics.modules.basicsgamemode

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("gamemode|gm")
@CommandPermission("basics.command.gamemode")
class GamemodeCommand(val module: BasicsGamemodeModule) : BaseCommand() {

    // SURVIVAL

    @Subcommand("survival|s|0")
    @CommandAlias("survival|gm0|gms")
    @CommandPermission("basics.command.gamemode.survival")
    fun onSurvivalSelf(player: Player) {
        set(player, player, GameMode.SURVIVAL)
    }

    @Subcommand("survival|s|0")
    @CommandAlias("survival|gm0|gms")
    @CommandPermission("basics.command.gamemode.survival.other")
    fun onSurvivalOther(sender: CommandSender, player: OnlinePlayer) {
        set(sender, player.player, GameMode.SURVIVAL)
    }

    // CREATIVE

    @Subcommand("creative|c|1")
    @CommandAlias("creative|gm1|gmc")
    @CommandPermission("basics.command.gamemode.creative")
    fun onCreativeSelf(player: Player) {
        set(player, player, GameMode.CREATIVE)
    }

    @Subcommand("creative|c|1")
    @CommandAlias("creative|gm1|gmc")
    @CommandPermission("basics.command.gamemode.creative.other")
    fun onCreativeOther(sender: CommandSender, player: OnlinePlayer) {
        set(sender, player.player, GameMode.CREATIVE)
    }

    // ADVENTURE

    @Subcommand("adventure|a|2")
    @CommandAlias("adventure|gm2|gma")
    @CommandPermission("basics.command.gamemode.adventure")
    fun onAdventureSelf(player: Player) {
        set(player, player, GameMode.ADVENTURE)
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("adventure|gm2|gma")
    @CommandPermission("basics.command.gamemode.adventure.other")
    fun onAdventureOther(sender: CommandSender, player: OnlinePlayer) {
        set(sender, player.player, GameMode.ADVENTURE)
    }

    // SPECTATOR
    @Subcommand("spectator|sp|3")
    @CommandAlias("spectator|gm3|gmsp")
    @CommandPermission("basics.command.gamemode.spectator")
    fun onSpectatorSelf(player: Player) {
        set(player, player, GameMode.SPECTATOR)
    }

    @Subcommand("spectator|sp|3")
    @CommandAlias("spectator|gm3|gmsp")
    @CommandPermission("basics.command.gamemode.spectator.other")
    fun onSpectatorOther(sender: CommandSender, player: OnlinePlayer) {
        set(sender, player.player, GameMode.SPECTATOR)
    }



    fun set(sender: CommandSender, player: Player, gameMode: GameMode) {
        player.gameMode = gameMode
        val message = if(sender == player.player) module.msgChangedSelf else module.msgChangedOthers

        message.tags(Placeholder.component("new-gamemode", module.getGameModeName(gameMode)))
            .concerns(player)

        module.audience.sender(sender).sendMessage(message.toComponent())
    }
}
