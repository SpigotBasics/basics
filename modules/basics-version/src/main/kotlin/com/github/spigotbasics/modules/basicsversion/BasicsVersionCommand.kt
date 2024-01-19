package com.github.spigotbasics.modules.basicsversion

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.PluginDescriptionFile

@CommandAlias("basics")
class BasicsVersionCommand(private val description: PluginDescriptionFile) : BaseCommand() {

    @Subcommand("version|v")
    fun version(sender: CommandSender) {
        sender.sendMessage(description.name)
        sender.sendMessage("Version ${description.version}")
        sender.sendMessage("Authors ${description.authors}")
        if (description.contributors.isNotEmpty()) {
            sender.sendMessage("Contributors ${description.contributors}")
        }
        if (description.website != null) {
            sender.sendMessage("Website ${description.website}")
        }
    }
}
