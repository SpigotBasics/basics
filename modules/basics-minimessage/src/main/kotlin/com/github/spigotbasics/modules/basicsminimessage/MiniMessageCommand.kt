package com.github.spigotbasics.modules.basicsminimessage

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("minimessage")
@CommandPermission("basics.minimessage")
class MiniMessageCommand(private val messageFactory: MessageFactory) : BaseCommand() {

    @Default
    fun message(sender: CommandSender, text: Array<String>) {
        val joined = text.joinToString(separator = " ")
        val player = sender as? Player
        messageFactory.createMessage(joined).concerns(player).sendToSender(sender)
    }

}
