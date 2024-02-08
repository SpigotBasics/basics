package com.github.spigotbasics.core.command

import org.bukkit.command.SimpleCommandMap

/**
 * Responsible for registering and keeping track of commands.
 *
 * @property serverCommandMap The server command map
 */

// TODO: Fix updateCommandsToPlayers logic
//   1. Must not be called async
//   2. in /reload, this would get called for each player and command, which is unnecessary
//      It's enough to call it once in onEnable for all online players, and otherwise only call it when a single
//      module is enabled/disabled, but not when they're bulk enabled/disabled
//   3. Modules that register a command later aren't added to the commandmap properly - gotta refresh that manually
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

    fun registerCommand(
        command: BasicsCommand,
        update: Boolean = true,
    ) {
        registeredCommands += command
        injectToServerCommandMap(command)
        if (update) {
            updateCommandsToPlayers()
        }
    }

    fun unregisterCommand(
        command: BasicsCommand,
        update: Boolean = true,
    ) {
        registeredCommands -= command
        removeFromServerCommandMap(command)
        if (update) {
            updateCommandsToPlayers()
        }
    }

    private fun updateCommandsToPlayers() {
        // TODO: This is only needed when changing commands AFTER the server has started
        //  It also doesn't seem to work - enabling a module later than startup doesn't add the command to the commandmap
        //  It can't be executed at all by players, but works fine from console??
//        try {
//            //if (Bukkit.isPrimaryThread()) {
//            Bukkit.getOnlinePlayers().forEach { player ->
//                player.updateCommands()
//            }
//            //}
//        } catch (_: Throwable) { }
    }

    private fun injectToServerCommandMap(command: BasicsCommand) {
        val success = serverCommandMap.register("basics", command)
        if (!success) {
            serverCommandMap.commands.forEach { existing ->
                if (existing.name == command.name) {
                    if (existing is BasicsCommand) {
                        existing.replaceCommand(command)
                    }
                }
            }
        }
    }

    private fun removeFromServerCommandMap(command: BasicsCommand) {
        // serverCommandMap.commands.remove(command)
        command.disableExecutor()
    }
}
