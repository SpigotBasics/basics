package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class MiniMessageCommand(private val messageFactory: MessageFactory)  {


    fun message(sender: CommandSender, text: Array<String>) {
        val joined = text.joinToString(separator = " ")
        val player = sender as? Player
        messageFactory.createMessage(joined).concerns(player).sendToSender(sender)
    }

}
