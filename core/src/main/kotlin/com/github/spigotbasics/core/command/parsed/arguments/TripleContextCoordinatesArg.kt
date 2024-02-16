package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.model.TripleContextCoordinates
import org.bukkit.command.CommandSender

class TripleContextCoordinatesArg(name: String) : CommandArgument<TripleContextCoordinates>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): TripleContextCoordinates? {
        return try {
            TripleContextCoordinates.parse(value)
        } catch (_: Exception) {
            null
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        // println(typing)
        // TODO: Add selectors and ~ ~~ for tabcomplete if has permission
        // TODO: That requires passing the concat-ed string to the tabComplete method
        return super.tabComplete(sender, typing)
    }

    override val greedy = true
    override val length = 3
}
