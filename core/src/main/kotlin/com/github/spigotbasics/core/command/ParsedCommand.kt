package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.command.arguments.ArgumentSignature
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ParsedCommand(
    val info: CommandInfo,
    val signatures: List<ArgumentSignature<*>>,
) : Command(info.name) {
    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        args: Array<out String>,
    ): Boolean {
        return false
    }
}
