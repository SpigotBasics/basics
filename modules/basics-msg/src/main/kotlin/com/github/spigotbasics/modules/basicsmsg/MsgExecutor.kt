package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MsgExecutor(private val module: BasicsMsgModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult {
        val sender = context.sender
        if(context.args.size < 2) {
            return CommandResult.USAGE
        }
        val player = requirePlayer(sender, context.args[0])
        val message = context.args.drop(1).joinToString(" ")
        if (sender is Player) {
            if (player.player == sender) {
                player2self(sender, message)
            } else {
                player2player(sender, player, message)
            }
        } else {
            console2player(sender, player, message)
        }
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        if(context.args.size == 1) {
            return null // null = normal list of players
        } else {
            return mutableListOf()
        }
    }

    private fun console2player(sender: CommandSender, player: Player, message: String) {
        val msg = module.formatConsole
        msg.tagUnparsed("message", message).sendToPlayer(player)
        module.formatSent.concerns(player).tagUnparsed("message", message).sendToSender(sender)
    }

    private fun player2player(
        sender: Player,
        receiver: Player,
        message: String
    ) {
        val receivedMsg = module.formatReceived
        val sentMsg = module.formatSent

        receivedMsg.concerns(sender).tagUnparsed("message", message).sendToPlayer(receiver)
        sentMsg.concerns(receiver).tagUnparsed("message", message).sendToPlayer(sender)
    }

    private fun player2self(
        player: Player,
        message: String
    ) {
        val msg = module.formatSelf
        msg.concerns(player).tagUnparsed("message", message).sendToPlayer(player)
    }

}
