package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

abstract class BasicsCommandExecutor(module: BasicsModule) {

    protected val coreMessages: CoreMessages = module.plugin.messages
    protected val messageFactory: MessageFactory = module.plugin.messageFactory

    abstract fun execute(context: BasicsCommandContext): CommandResult?

    open fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        return null
    }

    // TODO: Move these methods into own CommandParser object

    @Throws(BasicsCommandException::class)
    fun requirePlayer(sender: CommandSender, name: String): Player {
        val player = Bukkit.getPlayer(name)
        if(player == null) {
            throw BasicsCommandException(CommandResult.playerNotFound(name))
        }
        return player
    }

    @Throws(BasicsCommandException::class)
    fun notFromConsole(sender: CommandSender): Player {
        if(sender !is Player) {
            throw BasicsCommandException(CommandResult.notFromConsole())
        }
        return sender
    }

    @Throws(BasicsCommandException::class)
    fun requirePlayerOrMustSpecifyPlayerFromConsole(sender: CommandSender): Player {
        val player = sender as? Player
        if(player == null) {
            throw BasicsCommandException(CommandResult.mustBePlayerOrSpecifyPlayerFromConsole())
        }
        return player
    }

    @Throws(BasicsCommandException::class)
    fun failIfFlagsLeft(context: BasicsCommandContext) {
        if(context.flags.isEmpty()) return
        throw BasicsCommandException(CommandResult.unknownFlag(context.flags[0]))
    }

    @Throws(BasicsCommandException::class)
    fun failInvalidArgument(argument: String): CommandResult {
        throw BasicsCommandException(CommandResult.invalidArgument(argument))
    }

    fun requirePermission(sender: CommandSender, permission: Permission) {
        if(!sender.hasPermission(permission)) {
            throw BasicsCommandException(CommandResult.noPermission(permission))
        }
    }

}