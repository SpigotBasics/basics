package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.Location
import org.bukkit.command.CommandSender

data class BasicsCommandContext(
    val sender: CommandSender,
    val command: BasicsCommand,
    val label: String,
    val args: MutableList<String>,
    val location: Location?,
    val flags: MutableList<String> = mutableListOf(),
) {

}
