package com.github.spigotbasics.core.command

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContextExecutor
import com.github.spigotbasics.core.command.parsed.ParsedCommandExecutor
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class ParsedCommandBuilder<T : ParsedCommandContext>(
    private val messageFactory: MessageFactory,
    private val coreMessages: CoreMessages,
    private val commandManager: BasicsCommandManager,
    private val name: String,
    private val permission: Permission,
) {
    private var permissionMessage: Message = coreMessages.noPermission
    private var description: String? = null
    private var usage: String = ""
    private var aliases: List<String> = emptyList()
    private var executor: BasicsCommandExecutor? = null
    private var tabCompleter: BasicsTabCompleter? = null
    private var parsedExecutor: ParsedCommandContextExecutor<T>? = null
    private var argumentPaths: MutableList<ArgumentPath<T>> = mutableListOf()

    fun description(description: String) = apply { this.description = description }

    fun usage(usage: String) =
        apply {
            if (usage.startsWith("/")) error("Usage should not start with /<command> - only pass the arguments.")
            this.usage = usage
        }

    fun path(argumentPath: ArgumentPath<T>) = apply { this.argumentPaths.add(argumentPath) }

    fun paths(argumentPaths: List<ArgumentPath<T>>) = apply { this.argumentPaths.addAll(argumentPaths) }

    fun paths(vararg argumentPaths: ArgumentPath<T>) = apply { this.argumentPaths.addAll(argumentPaths) }

    fun executor(executor: ParsedCommandContextExecutor<T>) = apply { this.parsedExecutor = executor }

    private fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

    private fun executor(command: ParsedCommandExecutor<T>) =
        apply {
            this.executor =
                object : BasicsCommandExecutor(coreMessages, messageFactory) {
                    override fun execute(context: RawCommandContext): CommandResult? {
                        val result = command.execute(context.sender, context.args)

                        if (result is Either.Left) {
                            return result.value
                        }

                        if (result is Either.Right) {
                            val failure = result.value
                            // TODO: Proper messages
                            failure.errors.forEach { it.sendToSender(context.sender) }
                        }

                        return null
                    }

                    override fun tabComplete(context: RawCommandContext): MutableList<String> {
                        return command.tabComplete(context.sender, context.args).toMutableList()
                    }
                }
        }

    fun register(): BasicsCommand {
        val command = build()
        commandManager.registerCommand(command)
        return command
    }

    private fun build(): BasicsCommand {
        val command =
            ParsedCommandExecutor(
                parsedExecutor ?: error("parsedExecutor must be set"),
                argumentPaths,
            )
        executor(command)
        val info =
            CommandInfo(
                name = name,
                permission = permission,
                permissionMessage = permissionMessage,
                description = description,
                usage = usage,
                aliases = aliases,
            )
        return BasicsCommand(
            info = info,
            executor = executor ?: error("Executor must be set"),
            tabCompleter = tabCompleter ?: executor,
            coreMessages = coreMessages,
            messageFactory = messageFactory,
        )
    }
}
