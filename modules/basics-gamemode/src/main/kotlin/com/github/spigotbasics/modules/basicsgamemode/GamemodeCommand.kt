package com.github.spigotbasics.modules.basicsgamemode

import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class GamemodeCommand(val module: BasicsGamemodeModule) {

    // SURVIVAL
    fun onSurvivalSelf(player: Player) {
        set(player, player, GameMode.SURVIVAL)
    }

    fun onSurvivalOther(sender: CommandSender, player: Player) {
        set(sender, player, GameMode.SURVIVAL)
    }

    // CREATIVE
    fun onCreativeSelf(player: Player) {
        set(player, player, GameMode.CREATIVE)
    }

    fun onCreativeOther(sender: CommandSender, player: Player) {
        set(sender, player, GameMode.CREATIVE)
    }

    // ADVENTURE
    fun onAdventureSelf(player: Player) {
        set(player, player, GameMode.ADVENTURE)
    }

    fun onAdventureOther(sender: CommandSender, player: Player) {
        set(sender, player, GameMode.ADVENTURE)
    }

    // SPECTATOR
    fun onSpectatorSelf(player: Player) {
        set(player, player, GameMode.SPECTATOR)
    }

    fun onSpectatorOther(sender: CommandSender, player: Player) {
        set(sender, player, GameMode.SPECTATOR)
    }

    fun set(sender: CommandSender, player: Player, gameMode: GameMode) {
        player.gameMode = gameMode
        val message = if (sender == player.player) module.msgChangedSelf else module.msgChangedOthers

        message.tags("new-gamemode" to module.getName(gameMode))
            .concerns(player)

        message.sendToSender(sender)

    }
}
