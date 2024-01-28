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
            val permString = info.permission.name
            permission = permString
            description = info.description ?: ""
            usage = info.usage ?: "/$name"
            permissionMessage = info.permissionMessage.tagUnparsed("permission", permString).toLegacy()
        }

    override fun execute(sender: CommandSender, commandLabel: String, origArgs: Array<out String>?): Boolean {
        try {

            val args = origArgs?.toMutableList() ?: mutableListOf()
            val flags = mutableListOf<String>()

            // Parse "flags" (arguments that start with --)
            while (args.isNotEmpty() && args[0].startsWith("--")) {
                flags.add(args.removeAt(0))
            }

            val context = BasicsCommandContext(
                sender = sender,
                command = this,
                label = commandLabel,
                args = args,
                flags = flags,
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
        return info.permission.name
    }

}