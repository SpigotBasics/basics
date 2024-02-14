package com.github.spigotbasics.core.command

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.ArgumentPathBuilder
import com.github.spigotbasics.core.command.parsed.GenericArgumentPathBuilder
import com.github.spigotbasics.core.command.parsed.MapCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContextExecutor
import com.github.spigotbasics.core.command.parsed.ParsedCommandExecutor
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.command.raw.RawTabCompleter
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class ParsedCommandBuilder<T : ParsedCommandContext>(
    private val coreConfig: CoreConfig,
    private val messageFactory: MessageFactory,
    private val coreMessages: CoreMessages,
    private val commandManager: BasicsCommandManager,
    private val name: String,
    private val permission: Permission,
) {
    private var permissionMessage: Message = coreMessages.noPermission
    private var description: String? = null
    var usage: String = ""
    private var aliases: List<String> = emptyList()
    private var executor: BasicsCommandExecutor? = null
    private var tabCompleter: RawTabCompleter? = null
    private var parsedExecutor: ParsedCommandContextExecutor<T>? = null
    private var argumentPaths: MutableList<ArgumentPath<T>> = mutableListOf()

    fun description(description: String) = apply { this.description = description }

    fun usage(usage: String) =
        apply {
            if (usage.startsWith("/")) error("Usage should not start with /<command> - only pass the arguments.")
            this.usage = usage
        }

    fun path(argumentPath: ArgumentPath<T>) = apply { this.argumentPaths.add(argumentPath) }

    fun path(argumentPathBuilder: ArgumentPathBuilder<T>) = apply { this.argumentPaths.add(argumentPathBuilder.build()) }

    fun path(block: ArgumentPathBuilder<T>.() -> Unit): ArgumentPath<T> {
        val builder = GenericArgumentPathBuilder<T>()
        builder.block()
        return builder.build()
    }

    // fun paths(argumentPaths: List<ArgumentPath<T>>) = apply { this.argumentPaths.addAll(argumentPaths) }

    // fun paths(argumentPathBuilders: List<ArgumentPathBuilder<T>>) = apply { this.argumentPaths.addAll(argumentPathBuilders.map { it.build() }) }

    // fun paths(vararg argumentPaths: ArgumentPath<T>) = apply { this.argumentPaths.addAll(argumentPaths) }

    // fun paths(vararg argumentPathBuilders: ArgumentPathBuilder<T>) = apply { this.argumentPaths.addAll(argumentPathBuilders.map { it.build() }) }

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

    fun build(): BasicsCommand {
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
            coreConfig = coreConfig,
            coreMessages = coreMessages,
            messageFactory = messageFactory,
        )
    }
}
