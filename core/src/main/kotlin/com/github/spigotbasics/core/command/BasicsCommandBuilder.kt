package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.permissions.Permission

class BasicsCommandBuilder(
    private val module: BasicsModule,
    private val name: String,
    private val permission: Permission,
) {
    private var permissionMessage: Message = module.plugin.messages.noPermission
    private var description: String? = null
    private var usage: String = ""
    private var aliases: List<String> = emptyList()
    private var executor: BasicsCommandExecutor? = null
    private var tabCompleter: BasicsTabCompleter? = null

    fun description(description: String) = apply { this.description = description }

    fun usage(usage: String) =
        apply {
            if (usage.startsWith("/")) error("Usage should not start with /<command> - only pass the arguments.")
            this.usage = usage
        }

    fun permissionMessage(permissionMessage: Message) = apply { this.permissionMessage = permissionMessage }

    fun aliases(aliases: List<String>) = apply { this.aliases = aliases }

    fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

    fun tabCompleter(tabCompleter: BasicsTabCompleter) = apply { this.tabCompleter = tabCompleter }

    fun executor(executor: (RawCommandContext) -> CommandResult?) =
        apply {
            this.executor =
                object : BasicsCommandExecutor(module) {
                    override fun execute(context: RawCommandContext): CommandResult? {
                        return executor(context)
                    }
                }
        }

    fun tabCompleter(tabCompleter: (RawCommandContext) -> MutableList<String>?) =
        apply {
            this.tabCompleter =
                object : BasicsTabCompleter {
                    override fun tabComplete(context: RawCommandContext): MutableList<String>? {
                        return tabCompleter(context)
                    }
                }
        }

    fun register(): BasicsCommand {
        val command = build()
        module.commandManager.registerCommand(command)
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
            coreMessages = module.plugin.messages,
            messageFactory = module.plugin.messageFactory,
        )
    }
}
