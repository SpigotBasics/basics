package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.command.BasicsCommand
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.command.CommandSender

class MiniMessageExecutor(val messageFactory: MessageFactory) : BasicsCommandExecutor {
    override fun execute(sender: CommandSender, command: BasicsCommand, label: String, args: List<String>): Boolean {
        val message = args.joinToString(" ")
        if(message.isEmpty()) return false
        messageFactory.createMessage(message).sendToAllPlayers()
        return true
    }
}