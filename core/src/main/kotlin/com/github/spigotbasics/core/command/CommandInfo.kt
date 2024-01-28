package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.config.CoreMessages
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

// TODO: Executor does not belong into CommandInfo
// TODO: createCommandInfo in BasicsModule
// TODO: Permission message not shown when permission is missing, but "no command found" - make this configurable?
//       That'd require not setting any permission on the actual Command, but only checking it in the executor (BasicsCommand#execute)
data class CommandInfo (
    val name: String,
    val permission: String,
    val permissionMessage: Message,
    val description: String?,
    val usage: String?,
    val aliases: List<String>,
    //val executor: (CommandSender, BasicsCommand, String, List<String>) -> Boolean
    val executor: BasicsCommandExecutor
) {

    class Builder(private val messages: CoreMessages) {

        private var name: String? = null
        private var permission: String? = null
        private var permissionMessage: Message = messages.noPermission()
        private var description: String? = null
        private var usage: String? = null
        private var aliases: List<String> = emptyList()
        //private var executor: ((CommandSender, BasicsCommand, String, List<String>) -> Boolean)? = null
        private var executor: BasicsCommandExecutor? = null

        fun name(name: String) = apply { this.name = name }
        fun permission(permission: String) = apply { this.permission = permission }
        fun description(description: String) = apply { this.description = description }
        fun usage(usage: String) = apply { this.usage = usage }
        fun permissionMessage(permissionMessage: Message) = apply { this.permissionMessage = permissionMessage }
        fun aliases(aliases: List<String>) = apply { this.aliases = aliases }
        //fun executor(executor: (CommandSender, BasicsCommand, String, List<String>) -> Boolean) = apply { this.executor = executor }
        fun executor(executor: BasicsCommandExecutor) = apply { this.executor = executor }

        fun build() = CommandInfo(
            name = name ?: error("Name must be set"),
            permission = permission ?: error("Permission must be set"),
            permissionMessage = permissionMessage,
            description = description,
            usage = usage,
            aliases = aliases,
            executor = executor ?: error("Executor must be set")
        )

    }

}
