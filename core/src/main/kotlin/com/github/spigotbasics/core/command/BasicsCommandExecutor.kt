package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.config.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class BasicsCommandExecutor(val module: BasicsModule) {

    private val coreMessages: CoreMessages = module.plugin.messages
    protected val messageFactory: MessageFactory = module.plugin.messageFactory

    abstract fun execute(context: BasicsCommandContext): Boolean

    open fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        return null
    }

    fun requirePlayer(name: String): Player {
        return Bukkit.getPlayer(name) ?: throw BasicsCommandException("Player $name not found")
    }

    fun requirePlayerExact(name: String): Player {
        return Bukkit.getPlayerExact(name) ?: throw BasicsCommandException("Player $name not found")
    }

    fun requirePermission(sender: CommandSender, permission: String) {
        if(!sender.hasPermission(permission)) {
            coreMessages.noPermission(permission).sendToSender(sender)
            throw BasicsCommandException("${sender.name} does not have permission $permission")
        }
    }

}