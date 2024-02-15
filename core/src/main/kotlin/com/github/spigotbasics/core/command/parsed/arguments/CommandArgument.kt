package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

abstract class CommandArgument<T>(
    val name: String,
) {
    abstract fun parse(
        sender: CommandSender,
        value: String,
    ): T?

    open fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> = emptyList()

    // TODO: This is using the static Singleton :/
    open fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message = Basics.messages.invalidValueForArgument(name, value)

    /**
     * Whether this argument is greedy. If yes, it will consume all given strings until args.size - remainingArgs.
     * Only one argument can be greedy in a path.
     */
    open val greedy = false
}
