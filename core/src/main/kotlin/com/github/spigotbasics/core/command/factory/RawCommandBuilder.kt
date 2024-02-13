package com.github.spigotbasics.core.command.factory

import com.github.spigotbasics.core.command.BasicsCommand
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.raw.RawTabCompleter
import com.github.spigotbasics.core.command.CommandInfo
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class RawCommandBuilder(
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
    private var tabCompleter: RawTabCompleter? = null

    fun description(description: String) = apply { this.description = description }

    fun usage(usage: String) =
        apply {
            if (usage.startsWith("/")) error("Usage should not start with /<command> - only pass the arguments.")
            this.usage = usage
        }

    @Deprecated("Ignored by Bukkit")
    private fun permissionMessage(permissionMessage: Message) = apply { this.permissionMessage = permissionMessage }

    @Deprecated("Will be registered automatically in the future")
    fun aliases(aliases: List<String>) = apply { this.aliases = aliases }

    fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

    fun tabCompleter(tabCompleter: RawTabCompleter) = apply { this.tabCompleter = tabCompleter }

    fun executor(executor: (RawCommandContext) -> CommandResult?) =
        apply {
            this.executor =
                object : BasicsCommandExecutor(coreMessages, messageFactory) {
                    override fun execute(context: RawCommandContext): CommandResult? {
                        return executor(context)
                    }
                }
        }

    fun tabCompleter(tabCompleter: (RawCommandContext) -> MutableList<String>?) =
        apply {
            this.tabCompleter =
                object : RawTabCompleter {
                    override fun tabComplete(context: RawCommandContext): MutableList<String>? {
                        return tabCompleter(context)
                    }
                }
        }

    fun register(): BasicsCommand {
        val command = build()
        commandManager.registerCommand(command)
        return command
    }

    private fun build(): BasicsCommand {
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
