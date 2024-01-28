package com.github.spigotbasics.core.command

import org.bukkit.command.CommandSender

interface BasicsCommandExecutor {

    fun execute(sender: CommandSender, command: BasicsCommand, label: String, args: List<String>): Boolean

}