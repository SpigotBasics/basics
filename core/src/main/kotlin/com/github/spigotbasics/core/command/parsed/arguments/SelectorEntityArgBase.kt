package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.permission.CorePermissions
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

abstract class SelectorEntityArgBase<T>(name: String) : CommandArgument<T>(name) {
    protected enum class ErrorType {
        NOT_FOUND,
        NO_PERMISSION_SELECTORS,
        SELECTOR_INCLUDES_ENTITIES,
        MULTIPLE_PLAYERS_FOUND,
        MULTIPLE_ENTITIES_FOUND,
        COULD_NOT_PARSE_SELECTOR,
        NOT_FOUND_ENTITY,
    }

    protected val selectorPermission = CorePermissions.useSelectors

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

    // TODO: Additional canSee check for all matched players?
    protected fun get(
        sender: CommandSender,
        value: String,
        allowMultiple: Boolean,
        allowEntities: Boolean,
    ): Either<ErrorType, List<Entity>> {
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
        val selected =
            try {
                Bukkit.selectEntities(sender, value)
            } catch (e: IllegalArgumentException) {
                return Either.Left(ErrorType.COULD_NOT_PARSE_SELECTOR)
            }
        val entities = if (!allowEntities) selected.filterIsInstance<Player>() else selected
        if (!allowEntities && selected.size != entities.size) {
            return Either.Left(ErrorType.SELECTOR_INCLUDES_ENTITIES)
        }
        if (!allowMultiple && entities.size > 1) {
            return Either.Left(if (allowEntities) ErrorType.MULTIPLE_ENTITIES_FOUND else ErrorType.MULTIPLE_PLAYERS_FOUND)
        }
        if (entities.isEmpty()) {
            return Either.Left(if (allowEntities) ErrorType.NOT_FOUND_ENTITY else ErrorType.NOT_FOUND)
        }
        return Either.Right(entities)
    }

    protected fun errorMessage0(
        sender: CommandSender,
        value: String,
        allowMultiple: Boolean,
        allowEntities: Boolean,
    ): Message {
        return when (get(sender, value, allowMultiple, allowEntities).leftOrNull()) {
            ErrorType.NOT_FOUND -> Basics.messages.playerNotFound(value)
            ErrorType.NOT_FOUND_ENTITY -> Basics.messages.selectorMatchesNoEntities(name, value)
            ErrorType.NO_PERMISSION_SELECTORS -> Basics.messages.noPermission(selectorPermission)
            ErrorType.SELECTOR_INCLUDES_ENTITIES -> Basics.messages.selectorIncludesEntities(name, value)
            ErrorType.MULTIPLE_PLAYERS_FOUND -> Basics.messages.selectorMatchesMultiplePlayers(name, value)
            ErrorType.MULTIPLE_ENTITIES_FOUND -> Basics.messages.selectorMatchesMultipleEntities(name, value)
            ErrorType.COULD_NOT_PARSE_SELECTOR -> Basics.messages.selectorCouldNotParse(name, value)
            else -> super.errorMessage(sender, value)
        }
    }
}
