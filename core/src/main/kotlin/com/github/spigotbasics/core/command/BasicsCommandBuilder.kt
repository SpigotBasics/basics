package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.config.CoreMessages
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.command.CommandSender

class BasicsCommandBuilder(private val module: BasicsModule) {

    private var name: String? = null
    private var permission: String? = null
    private var permissionMessage: Message = module.plugin.messages.noPermission()
    private var description: String? = null
    private var usage: String? = null
    private var aliases: List<String> = emptyList()
    private var executor: BasicsCommandExecutor? = null

    fun name(name: String) = apply { this.name = name }
    fun permission(permission: String) = apply { this.permission = permission }
    fun description(description: String) = apply { this.description = description }
    fun usage(usage: String) = apply { this.usage = usage }
    fun permissionMessage(permissionMessage: Message) = apply { this.permissionMessage = permissionMessage }
    fun aliases(aliases: List<String>) = apply { this.aliases = aliases }
    fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

    fun executor(executor: (BasicsCommandContext) -> Boolean) = apply {
        this.executor = object : BasicsCommandExecutor(module) {
            override fun execute(context: BasicsCommandContext): Boolean {
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
            name = name ?: error("Name must be set"),
            permission = permission ?: error("Permission must be set"),
            permissionMessage = permissionMessage,
            description = description,
            usage = usage,
            aliases = aliases,
        )
        return BasicsCommand(info, executor ?: error("Executor must be set"))
    }


}