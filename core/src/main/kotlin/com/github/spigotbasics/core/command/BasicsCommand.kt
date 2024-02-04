package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.CoreMessages
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity


/**
 * Represents a registered command. This is what is actually registered to Bukkit.
 *
 * @property info Command info
 * @property executor Command executor
 */
class BasicsCommand internal constructor (
    var info: CommandInfo,
    private var executor: BasicsCommandExecutor?,
    val coreMessages: CoreMessages
) :
    Command(info.name) {

        init {
            val permString = info.permission.name
            permission = permString
            description = info.description ?: ""
            usage = info.usage ?: "/$name"
            permissionMessage = info.permissionMessage.tagUnparsed("permission", permString).toLegacyString()
        }

    override fun execute(sender: CommandSender, commandLabel: String, origArgs: Array<out String>?): Boolean {
        try {

            val args = origArgs?.toMutableList() ?: mutableListOf()

            val context = BasicsCommandContext(
                sender = sender,
                command = this,
                label = commandLabel,
                args = args,
                location = if (sender is Entity) sender.location else null
            )

            if(executor == null) {
                coreMessages.commandModuleDisabled.sendToSender(sender)
                return true
            }

            val returned = executor!!.execute(context)

            if (!returned) {
                sender.sendMessage("Usage: $usage") // TODO: Use messages instead of Strings
                return false
            }

            return true

        } catch (e: BasicsCommandException) {

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

    /**
     * Disable this command's executor by setting it to null. Disabled commands will always return true and
     * print a message to the sender.
     *
     */
    fun disableExecutor() {
        executor = null
    }

}