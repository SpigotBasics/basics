package com.github.spigotbasics.core.command

abstract class CommandResult private constructor() {

    abstract fun process(context: BasicsCommandContext)

    companion object {
        val SUCCESS = object : CommandResult() {
            override fun process(context: BasicsCommandContext) {
                // Do nothing
            }
        }

        fun usage(/*messageFactory: MessageFactory, */usage: String): CommandResult {
            return object : CommandResult() {
                override fun process(context: BasicsCommandContext) {
                    context.command.messageFactory
                        .createMessage(
                            "<red>Invalid command usage.</red>",
                            "<red>Usage: </red><gold>/<#command> <#usage></gold>"
                        ).tagParsed("usage", usage).tagParsed("command", context.command.name)
                        .sendToSender(context.sender)
                }
            }

        }
    }

}