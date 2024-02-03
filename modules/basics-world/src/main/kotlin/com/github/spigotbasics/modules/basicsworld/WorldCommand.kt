package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.util.TeleportUtils
import org.bukkit.Bukkit
import org.bukkit.World

class WorldCommand(val module: BasicsWorldModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        val player = notFromConsole(context.sender)
        val args = context.args
        if (args.isEmpty()) {
            return false
        }
        val origin = player.location
        val newWorld = getWorld(args[0])
        if (newWorld == null) {
            coreMessages.worldNotFound(args[0]).sendToSender(player)
            return true
        }

        val translatedTargetLocation = TeleportUtils.getScaledLocationInOtherWorld(origin, newWorld)
        val future = TeleportUtils.getSafeTeleportLocationAsync(translatedTargetLocation, 16)

        future.thenAccept { safeLocation ->
            if (safeLocation == null) {
                println("No safe location found")
                coreMessages.noSafeLocationFound.sendToSender(player)
            } else {
                println("Teleporting!")
                try {
                    module.scheduler.runTask {
                        try {
                            Spiper.teleportAsync(player, safeLocation)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.exceptionally { e ->
            e.printStackTrace()
            null
        }
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
            return allWorldsAnd012().partialMatches(args[0])
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
