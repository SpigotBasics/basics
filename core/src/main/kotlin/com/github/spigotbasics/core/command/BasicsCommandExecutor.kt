package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

abstract class BasicsCommandExecutor(module: BasicsModule) {

    private val coreMessages: CoreMessages = module.plugin.messages
    protected val messageFactory: MessageFactory = module.plugin.messageFactory

    abstract fun execute(context: BasicsCommandContext): Boolean

    open fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        return null
    }

    // TODO: Move these methods into own CommandParser object

    @Throws(BasicsCommandException::class)
    fun requirePlayer(sender: CommandSender, name: String): Player {
        val player = Bukkit.getPlayer(name)
        if(player == null) {
            coreMessages.playerNotFound(name).sendToSender(sender)
            throw BasicsCommandException("Player $name not found")
        }
        return player
    }

    @Throws(BasicsCommandException::class)
    fun requirePlayerExact(sender: CommandSender, name: String): Player {
        val player = Bukkit.getPlayerExact(name)
        if(player == null) {
            coreMessages.playerNotFound(name).sendToSender(sender)
            throw BasicsCommandException("Player $name not found")
        }
        return player
    }

    @Throws(BasicsCommandException::class)
    fun requirePlayerOrMustSpecifyPlayerFromConsole(sender: CommandSender): Player {
        val player = sender as? Player
        if(player == null) {
            coreMessages.mustSpecifyPlayerFromConsole.sendToSender(sender)
            throw BasicsCommandException("Must specify player from console")
        }
        return player
    }

    @Throws(BasicsCommandException::class)
    fun failIfFlagsLeft(context: BasicsCommandContext) {
        if(context.flags.isEmpty()) return
        coreMessages.unknownOption(context.flags[0]).sendToSender(context.sender)
        throw BasicsCommandException("Unknown option: ${context.flags[0]}")
    }

    @Throws(BasicsCommandException::class)
    fun failInvalidArgument(sender: CommandSender, argument: String): Boolean {
        coreMessages.invalidArgument(argument).sendToSender(sender)
        throw BasicsCommandException("Invalid argument: $argument")
    }

    fun requirePermission(sender: CommandSender, permission: Permission) {
        if(!sender.hasPermission(permission)) {
            coreMessages.noPermission(permission).sendToSender(sender)
            throw BasicsCommandException("${sender.name} does not have permission $permission")
        }
    }

}