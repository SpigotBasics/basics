package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.libraries.co.aikar.commands.BaseCommand
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.CommandAlias
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.CommandPermission
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.Default
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.Description
import com.github.spigotbasics.libraries.co.aikar.commands.bukkit.contexts.OnlinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("msg|dm|pm|tell|whisper|w|message")
@Description("Sends a private message to another player")
@CommandPermission("basics.command.msg")
class MsgCommand(private val module: BasicsMsgModule) : BaseCommand() {

    @Default
    fun onMsg(sender: CommandSender, player: OnlinePlayer, message: String) {
        if (sender is Player) {
            if (player.player == sender) {
                player2self(sender, message)
            } else {
                player2player(sender, player.player, message)
            }
        } else {
            console2player(sender, player.player, message)
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