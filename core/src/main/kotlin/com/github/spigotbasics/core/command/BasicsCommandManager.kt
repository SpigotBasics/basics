package com.github.spigotbasics.core.command

import org.bukkit.Bukkit
import org.bukkit.command.SimpleCommandMap

/**
 * Responsible for registering and keeping track of commands.
 *
 * @property serverCommandMap The server command map
 */
class BasicsCommandManager(private val serverCommandMap: SimpleCommandMap) {

    private val registeredCommands: MutableList<BasicsCommand> = mutableListOf()

    fun registerAll(commands: List<BasicsCommand>) {
        commands.forEach { command ->
            registerCommand(command, false)
        }
        updateCommandsToPlayers()
    }

    fun unregisterAll() {
        registeredCommands.toList().forEach { command ->
            unregisterCommand(command, false)
        }
        updateCommandsToPlayers()
    }

    fun registerCommand(command: BasicsCommand, update: Boolean = true) {
        registeredCommands += command
        injectToServerCommandMap(command)
        if (update) {
            updateCommandsToPlayers()
        }
    }

    fun unregisterCommand(command: BasicsCommand, update: Boolean = true) {
        registeredCommands -= command
        removeFromServerCommandMap(command)
        if (update) {
            updateCommandsToPlayers()
        }
    }

    private fun updateCommandsToPlayers() {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.updateCommands()
        }
    }

    private fun injectToServerCommandMap(command: BasicsCommand) {
        val success = serverCommandMap.register("basics", command)
        if(!success) {
            serverCommandMap.commands.forEach { existing ->
                if(existing.name == command.name) {
                    if(existing is BasicsCommand) {
                        existing.replaceCommand(command)
                    }
                }
            }
        }
    }

    private fun removeFromServerCommandMap(command: BasicsCommand) {
        //serverCommandMap.commands.remove(command)
        command.disableExecutor()
    }


}
