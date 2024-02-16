package com.github.spigotbasics.modules.basicscore.commands

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.extensions.toSnbtWithType
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateGiveCommand(val messageFactory: MessageFactory) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        sender as Player
        val item = sender.inventory.itemInMainHand
        val command = "/basics:give @p ${item.toSnbtWithType()} ${item.amount}"

        val msg =
            messageFactory.createMessage(
                "<click:suggest_command:'<#command>'><yellow>/give command for this item <gray>(click to copy)</gray>:</yellow>\n" +
                    "<#command></click>",
            ).tagParsed("command", command)
        msg.sendToSender(sender)
        Bukkit.getConsoleSender().sendMessage(command)
    }
}
