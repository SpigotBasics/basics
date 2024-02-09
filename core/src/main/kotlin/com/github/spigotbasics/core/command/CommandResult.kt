package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.Message
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

abstract class CommandResult private constructor() {
    abstract fun process(context: BasicsCommandContext)

    @Throws(BasicsCommandException::class)
    fun asException(): BasicsCommandException {
        return BasicsCommandException(this)
    }

    companion object {
        private fun fromContext(action: ((BasicsCommandContext) -> Unit)): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    action(context)
                }
            }
        }

        fun fromMessage(action: ((CoreMessages) -> Message)): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    action(context.command.coreMessages).sendToSender(context.sender)
                }
            }
        }

        val SUCCESS = fromContext { _ -> }

        val USAGE = usage()

        val NOT_FROM_CONSOLE =
            fromMessage { msg ->
                msg.commandNotFromConsole
            }

        val MUST_BE_PLAYER_OR_SPECIFY_PLAYER_FROM_CONSOLE =
            fromMessage { msg ->
                msg.mustSpecifyPlayerFromConsole
            }

        val NO_ITEM_IN_HAND =
            fromMessage { msg ->
                msg.notHavingItemInHand
            }

        fun usage(usage: String? = null): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.messageFactory
                        // TODO: Configurable
                        .createMessage(
                            "<red>Invalid command usage.</red>",
                            "<red>Usage: </red><gold>/<#command> <#usage></gold>",
                        ).tagUnparsed("usage", usage ?: context.command.info.usage)
                        .tagUnparsed("command", context.command.name)
                        .sendToSender(context.sender)
                }
            }
        }

        fun playerNotFound(name: String): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.playerNotFound(name).sendToSender(context.sender)
                }
            }
        }

        fun unknownFlag(flag: String): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.unknownOption(flag).sendToSender(context.sender)
                }
            }
        }

        fun invalidArgument(argument: String): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.invalidArgument(argument).sendToSender(context.sender)
                }
            }
        }

        fun noPermission(permission: Permission): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.noPermission(permission).sendToSender(context.sender)
                }
            }
        }

        fun noItemInHandOthers(player: Player): CommandResult {
            return fromMessage { msg ->
                msg.notHavingItemInHand(player)
            }
        }
    }
}
