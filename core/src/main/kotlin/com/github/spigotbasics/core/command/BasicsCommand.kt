package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.extensions.debug
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity

class BasicsCommand(
    var info: CommandInfo,
    private var executor: BasicsCommandExecutor?,
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

            debug("Executing command $name with label $commandLabel and args ${origArgs?.joinToString(", ")}")

            val args = origArgs?.toMutableList() ?: mutableListOf()
            //val flags = mutableListOf<String>()

            // Parse "flags" (arguments that start with --)
//            while (args.isNotEmpty() && args[0].startsWith("--")) {
//                flags.add(args.removeAt(0))
//            }

            val context = BasicsCommandContext(
                sender = sender,
                command = this,
                label = commandLabel,
                args = args,
                location = if (sender is Entity) sender.location else null
            )

            if(executor == null) {
                sender.sendMessage(ChatColor.RED.toString() + "The module that registered this command has been disabled.")
                return true
            }

            val returned = executor!!.execute(context)

            if (!returned) {
                debug("Command Executor returned false, printing usage now...")
                sender.sendMessage("Usage: $usage") // TODO: Use messages instead of Strings
                debug("And returning false now, too")
                return false
            }

            debug("Command Executor returned true, returning true now")
            return true

        } catch (e: BasicsCommandException) {
            debug("BasicsCommandException: ${e.message}")
            return true
        }
    }

    internal fun replaceCommand(command: BasicsCommand) {
        this.executor = command.executor
        this.info = command.info
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
        if(executor == null) return mutableListOf()
        val result = executor!!.tabComplete(context)
        return result ?: super.tabComplete(sender, alias, args, location)
    }

    override fun getPermission(): String {
        return info.permission.name
    }

    fun disableExecutor() {
        executor = null
    }

}