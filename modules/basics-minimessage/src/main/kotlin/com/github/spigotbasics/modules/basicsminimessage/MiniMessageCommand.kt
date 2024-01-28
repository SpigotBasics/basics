package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.config.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class MiniMessageCommand(
    private val coreMessages: CoreMessages,
    private val messageFactory: MessageFactory
) : Command("minimessage", "Broadcasts a MiniMessage-formatted message", "/minimessage <message>", listOf("bcmm")) {

    fun message(sender: CommandSender, text: Array<String>) {
        val joined = text.joinToString(separator = " ")
        val player = sender as? Player
        messageFactory.createMessage(joined).concerns(player).sendToSender(sender)
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if(!sender.hasPermission("basics.command.minimessage")) {
            coreMessages.noPermission("basics.command.minimessage").sendToSender(sender)
            return true
        }
        if(args == null || args.isEmpty()) {
            return false
        }
        messageFactory.createMessage(args.joinToString(" ")).sendToAllPlayers()
        return true
    }

}
