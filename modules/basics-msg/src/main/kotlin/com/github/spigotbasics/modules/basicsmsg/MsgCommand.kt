package com.github.spigotbasics.modules.basicsmsg

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MsgCommand(private val module: BasicsMsgModule) {

    fun onMsg(sender: CommandSender, player: Player, message: String) {
        if (sender is Player) {
            if (player.player == sender) {
                player2self(sender, message)
            } else {
                player2player(sender, player, message)
            }
        } else {
            console2player(sender, player, message)
        }
    }

    private fun console2player(sender: CommandSender, player: Player, message: String) {
        val msg = module.formatConsole
        msg.concerns(player).tags("message" to message).sendToPlayer(player)
    }

    private fun player2player(
        sender: Player,
        receiver: Player,
        message: String
    ) {
        val receivedMsg = module.formatReceived
        val sentMsg = module.formatSent

        receivedMsg.concerns(sender).tags("message" to message).sendToPlayer(receiver)
        sentMsg.concerns(receiver).tags("message" to message).sendToPlayer(sender)
    }

    private fun player2self(
        player: Player,
        message: String
    ) {
        val msg = module.formatSelf
        msg.concerns(player).tags("message" to message).sendToPlayer(player)
    }

}