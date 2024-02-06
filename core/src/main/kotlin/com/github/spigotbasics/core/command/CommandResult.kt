package com.github.spigotbasics.core.command

import org.bukkit.permissions.Permission

abstract class CommandResult private constructor() {

    abstract fun process(context: BasicsCommandContext)

    companion object {

        val SUCCESS = object : CommandResult() {
            override fun process(context: BasicsCommandContext) {
                // Do nothing
            }
        }

        val USAGE = usage()
        val NOT_FROM_CONSOLE = notFromConsole()
        val MUST_BE_PLAYER_OR_SPECIFY_PLAYER_FROM_CONSOLE = mustBePlayerOrSpecifyPlayerFromConsole()

        fun usage(usage: String? = null): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.messageFactory
                        .createMessage( // TODO: Configurable
                            "<red>Invalid command usage.</red>",
                            "<red>Usage: </red><gold>/<#command> <#usage></gold>"
                        ).tagUnparsed("usage", usage ?: context.command.info.usage).tagUnparsed("command", context.command.name)
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

        fun notFromConsole(): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.commandNotFromConsole.sendToSender(context.sender)
                }
            }
        }

        fun mustBePlayerOrSpecifyPlayerFromConsole(): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.coreMessages.mustSpecifyPlayerFromConsole.sendToSender(context.sender)
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
    }

}