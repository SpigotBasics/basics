package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.addAnd
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.Bukkit
import org.bukkit.World
import java.util.logging.Level

class WorldCommand(val module: BasicsWorldModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        val player = notFromConsole(context.sender)
        val args = context.args
        if (args.isEmpty()) {
            return false
        }

        context.readFlags()
        val force = (context.popFlag("-f") or context.popFlag("--force"))

        val origin = player.location
        val newWorld = getWorld(args[0])
        if (newWorld == null) {
            coreMessages.worldNotFound(args[0]).sendToSender(player)
            return true
        }

        if (newWorld == origin.world) {
            module.msgAlreadyInWorld(newWorld.name).sendToSender(player)
            return true
        }

        val translatedTargetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, newWorld)

        if(force) {
            Spiper.teleportAsync(player, translatedTargetLocation)
            return true
        }

        val future = TeleportUtils.findSafeLocationInSameChunkAsync(
            translatedTargetLocation,
            newWorld.minHeight,
            newWorld.maxHeight
        )

        future.thenAccept { safeLocation ->
            if (safeLocation == null) {
                coreMessages.noSafeLocationFound.sendToSender(player)
                module.logger.warning("NO SAFE LOCATION FOUND!!!")
            } else {
                module.scheduler.runTask {
                    Spiper.teleportAsync(player, safeLocation).whenComplete { success, throwable ->
                        if (throwable != null || !success) {
                            module.msgUnsuccessful(newWorld.name).sendToSender(player)
                            module.logger.warning("Could not teleport player to world ${newWorld.name}")
                            throwable?.let {
                                module.logger.log(
                                    Level.SEVERE,
                                    "Could not teleport player to world ${newWorld.name}",
                                    it
                                )
                            }
                        } else if (success) {
                            module.logger.info("Teleported player ${player.name} to world ${newWorld.name}")
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
        return true
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

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        val args = context.args
        if (args.size == 1) {
            return allWorldsAnd012().addAnd("--force").addAnd("-f").partialMatches(args[0])
        }
        if(args.size == 2) {
            if(args[0] == "--force" || args[0] == "-f") {
                return allWorldsAnd012().partialMatches(args[1])
            }
        }
        return mutableListOf()
    }

    private fun allWorldsAnd012(): MutableList<String> {
        val list = Bukkit.getWorlds().map { it.name }.toMutableList()
        list.add("0")
        list.add("1")
        list.add("2")
        return list
    }

}
