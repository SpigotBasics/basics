package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ParsedCommand : Command("give") {

    val argumentList: List<Pair<String, Argument<*>>> = listOf(
        "receiver" to ArgumentPlayer,
        "material" to ArgumentMaterial,
    )


    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val argsList = args.toList()
        argsList.forEachIndexed() { index, arg ->
            val argument = argumentList[index]
            val valueName = argument.first
            val value = argument.second.parse(arg)
        }
        // But how do I turn this into a GiveCommandContext now??
    }
}