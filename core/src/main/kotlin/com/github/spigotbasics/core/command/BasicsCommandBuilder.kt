package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.permissions.Permission

class BasicsCommandBuilder(
    private val module: BasicsModule,
    private val name: String,
    private val permission: Permission
) {

    private var permissionMessage: Message = module.plugin.messages.noPermission
    private var description: String? = null
    private var usage: String? = null
    private var aliases: List<String> = emptyList()
    private var executor: BasicsCommandExecutor? = null

    fun description(description: String) = apply { this.description = description }
    fun usage(usage: String) = apply { this.usage = usage }
    fun permissionMessage(permissionMessage: Message) = apply { this.permissionMessage = permissionMessage }
    fun aliases(aliases: List<String>) = apply { this.aliases = aliases }
    fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

    fun executor(executor: (BasicsCommandContext) -> CommandResult?) = apply {
        this.executor = object : BasicsCommandExecutor(module) {
            override fun execute(context: BasicsCommandContext): CommandResult? {
                return executor(context)
            }
        }
    }

    fun register(): BasicsCommand {
        val command = build()
        module.commandManager.registerCommand(command, true)
        return command
    }

    private fun build(): BasicsCommand {
        val info = CommandInfo(
            name = name,
            permission = permission,
            permissionMessage = permissionMessage,
            description = description,
            usage = usage,
            aliases = aliases,
        )
        return BasicsCommand(
            info,
            executor ?: error("Executor must be set"),
            module.plugin.messages,
            module.plugin.messageFactory
        )
    }


}