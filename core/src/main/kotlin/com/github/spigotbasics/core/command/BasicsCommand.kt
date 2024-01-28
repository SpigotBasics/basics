package com.github.spigotbasics.core.command

import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class BasicsCommand(
    val info: CommandInfo,
    val executor: BasicsCommandExecutor,
) :
    Command(info.name) {

        init {
            permission = info.permission
            description = info.description ?: ""
            usage = info.usage ?: "/$name"
            permission = info.permission
        }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        try {

            val context = BasicsCommandContext(
                sender = sender,
                command = this,
                label = commandLabel,
                args = args?.toMutableList() ?: mutableListOf(),
                location = if (sender is Entity) sender.location else null
            )

            return executor.execute(context)

        } catch (_: BasicsCommandException) {
            // TODO: Print out when debug is enabled
            return true
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>?,
        location: Location?
    ): MutableList<String> {
        val context = BasicsCommandContext(
            sender = sender,
            command = this,
            label = alias,
            args = args?.toMutableList() ?: mutableListOf(),
            location = location
        )
        val result = executor.tabComplete(context)
        return result ?: super.tabComplete(sender, alias, args, location)
    }

    override fun getPermission(): String {
        return info.permission
    }

    override fun getPermissionMessage(): String {
        return info.permissionMessage.tagUnparsed("permission", permission).toLegacy()
    }
}