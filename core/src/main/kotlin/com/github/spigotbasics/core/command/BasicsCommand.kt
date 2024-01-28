package com.github.spigotbasics.core.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BasicsCommand(val info: CommandInfo) :
    Command(info.name) {

        init {
            permission = info.permission
            description = info.description ?: ""
            usage = info.usage ?: "/$name"
            permission = info.permission
        }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        return info.executor.execute(sender, this, commandLabel, args?.toList() ?: emptyList())
    }

    override fun getPermission(): String {
        return info.permission
    }

    override fun getPermissionMessage(): String {
        return info.permissionMessage.tagUnparsed("permission", permission).toLegacy()
    }
}