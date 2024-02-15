package com.github.spigotbasics.core.listeners

import com.github.spigotbasics.core.command.common.BasicsCommand
import org.bukkit.command.CommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandSendEvent

class PlayerCommandListSendListener(private val commandMap: CommandMap) : Listener {
    @EventHandler
    fun onPlayerCommandListSend(event: PlayerCommandSendEvent) {
        event.commands.removeIf {
            val basicsCmd = getBasicsCommand(it)
            basicsCmd != null && !event.player.hasPermission(basicsCmd.info.permission)
        }
    }

    fun getBasicsCommand(commandName: String): BasicsCommand? {
        val command = commandMap.getCommand(commandName)
        return if (command is BasicsCommand) {
            command
        } else {
            null
        }
    }
}
