package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.extensions.lastOrEmpty
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.model.TripleContextCoordinates
import org.bukkit.command.CommandSender

class TripleContextCoordinatesArg(name: String) : CommandArgument<TripleContextCoordinates>(name) {
    companion object {
        val logger = BasicsLoggerFactory.getCoreLogger(TripleContextCoordinatesArg::class)
    }

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
        logger.debug(1, typing)

        val split = typing.split(" ")
        if (split.size > 5) return emptyList()
        for (previous in split.dropLast(1)) {
            if (!TripleContextCoordinates.isValidSinglePart(previous)) {
                return emptyList()
            }
        }
        return listOf("~", "~~").partialMatches(split.lastOrEmpty())
    }

    override val greedy = true
    override val length = 3
}
