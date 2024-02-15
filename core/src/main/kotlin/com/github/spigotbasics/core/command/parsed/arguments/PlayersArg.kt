package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.common.leftOrNull
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayersArg(name: String) : CommandArgument<List<Player>>(name) {
    private enum class ErrorType {
        NOT_FOUND,
        NO_PERMISSION_SELECTORS,
        SELECTOR_INCUDES_ENTITIES,
    }

    private val selectorPermission = Basics.permissions.useSelectors

    override fun parse(
        sender: CommandSender,
        value: String,
    ): List<Player>? {
        return get(sender, value).fold(
            { _ -> null },
            { it },
        )
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return Bukkit.getOnlinePlayers().filter {
            if (sender is Player) {
                sender.canSee(it)
            } else {
                true
            }
        }.map { it.name }.partialMatches(typing)
    }

    private fun get(
        sender: CommandSender,
        value: String,
    ): Either<ErrorType, List<Player>> {
        val onePlayer = Bukkit.getPlayer(value)
        if (onePlayer != null) {
            return Either.Right(listOf(onePlayer))
        }
        if (!value.startsWith("@")) {
            return Either.Left(ErrorType.NOT_FOUND)
        }
        if (!sender.hasPermission(selectorPermission)) {
            return Either.Left(ErrorType.NO_PERMISSION_SELECTORS)
        }
        val selected = Bukkit.selectEntities(sender, value)
        val players = selected.filterIsInstance<Player>()
        if (selected.size != players.size) {
            return Either.Left(ErrorType.SELECTOR_INCUDES_ENTITIES)
        }
        if (players.isEmpty()) {
            return Either.Left(ErrorType.NOT_FOUND)
        }
        return Either.Right(players)
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        return when (get(sender, value).leftOrNull()) {
            ErrorType.NOT_FOUND -> Basics.messages.playerNotFound(value)
            ErrorType.NO_PERMISSION_SELECTORS -> Basics.messages.noPermission(selectorPermission)
            ErrorType.SELECTOR_INCUDES_ENTITIES -> Basics.messages.selectorIncludesEntities(value)
            else -> super.errorMessage(sender, value)
        }
    }
}
