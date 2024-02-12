package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.RawCommandContext
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.util.TeleportUtils
import com.github.spigotbasics.core.util.WorldUtils
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.permissions.Permissible
import java.util.logging.Level

class WorldCommand(val module: BasicsWorldModule) : BasicsCommandExecutor(module) {
    override fun execute(context: RawCommandContext): CommandResult {
        val player = notFromConsole(context.sender)
        val args = context.args
        if (args.isEmpty()) {
            return CommandResult.USAGE
        }

        context.readFlags()
        val force = (context.popFlag("-f") or context.popFlag("--force"))

        val origin = player.location
        val newWorld = getWorld(args[0])
        if (newWorld == null) {
            coreMessages.worldNotFound(args[0]).sendToSender(player)
            return CommandResult.SUCCESS // TODO: CommandResult.worldNotFound(String)
        }

        if (newWorld == origin.world) {
            module.msgAlreadyInWorld(newWorld.name).sendToSender(player)
            return CommandResult.SUCCESS
        }

        val translatedTargetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, newWorld)

        requirePermission(player, module.getWorldPermission(newWorld.name))

        if (force) {
            Spiper.teleportAsync(player, translatedTargetLocation)
            return CommandResult.SUCCESS
        }

        val future =
            TeleportUtils.findSafeLocationInSameChunkAsync(
                translatedTargetLocation,
                newWorld.minHeight,
                newWorld.maxHeight,
            )

        future.thenAccept { safeLocation ->

            module.messageFactory.createPlainMessage("").sendToPlayerActionBar(player)

            if (safeLocation == null) {
                coreMessages.noSafeLocationFound.sendToSender(player)
            } else {
                module.scheduler.runTask {
                    Spiper.teleportAsync(player, safeLocation).whenComplete { success, throwable ->
                        if (throwable != null || !success) {
                            module.msgUnsuccessful(newWorld.name).sendToSender(player)
                            throwable?.let {
                                module.logger.log(
                                    Level.SEVERE,
                                    "Could not teleport player to world ${newWorld.name}",
                                    it,
                                )
                            }
                        } else if (success) {
                            module.msgSuccess(newWorld.name).sendToSender(player)
                        }
                    }
                }
            }
        }.exceptionally { throwable ->
            module.logger.log(Level.SEVERE, "Could not find safe location for player", throwable)
            module.msgUnsuccessful(newWorld.name).sendToSender(player)
            null
        }

        module.msgStartingTeleport(newWorld.name).sendToPlayerActionBar(player)
        return CommandResult.SUCCESS
    }

    fun getWorld(name: String): World? {
        val worlds = Bukkit.getWorlds()
        return when (name) {
            "0" -> worlds[0]
            "1" -> worlds.find { it.environment == World.Environment.NETHER }
            "2" -> worlds.find { it.environment == World.Environment.THE_END }
            else -> Bukkit.getWorld(name)
        }
    }

    override fun tabComplete(context: RawCommandContext): MutableList<String> {
        val args = context.args
        if (args.size == 1) {
            return allWorldsAnd012(context.sender).apply {
                if (isNotEmpty()) {
                    add("--force")
                    add("-f")
                }
            }.partialMatches(args[0])
        }
        if (args.size == 2) {
            if (args[0] == "--force" || args[0] == "-f") {
                return allWorldsAnd012(context.sender).partialMatches(args[1])
            }
        }
        return mutableListOf()
    }

    private fun allWorldsAnd012(sender: Permissible): MutableList<String> {
        val list =
            Bukkit.getWorlds().filter {
                val hasPerm = sender.hasPermission(module.getWorldPermission(it.name))
                return@filter hasPerm
            }.map { it.name }.toMutableList()

        if (list.contains(WorldUtils.defaultWorldName)) {
            list.add("0")
        }
        if (list.contains(WorldUtils.netherWorldName)) {
            list.add("1")
        }
        if (list.contains(WorldUtils.endWorldName)) {
            list.add("2")
        }
        return list
    }
}
