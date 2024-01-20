package com.github.spigotbasics.plugin

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import org.bukkit.command.CommandSender

@CommandAlias("basics")
@Description("The base basics command")
class BasicsCommand : BaseCommand() {

    @Default
    fun defaultBasics(sender: CommandSender) {
        sender.sendMessage("Hello, world!")
    }
}
