package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.Bukkit
import org.bukkit.World

class WorldCommand(module: BasicsWorldModule) : BasicsCommandExecutor(module){
    override fun execute(context: BasicsCommandContext): Boolean {
        val player = notFromConsole(context.sender)
        val args = context.args
        if (args.isEmpty()) {
            return false
        }
        val world = getWorld(args[0])
        if (world == null) {
            coreMessages.worldNotFound(args[0]).sendToSender(player)
            return true
        }

        // TODO: Safe teleport

        TODO()

    }

    fun getWorld(name: String): World? {
        val worlds = Bukkit.getWorlds()
        return when(name){
            "0" -> Bukkit.getWorlds()[0]
            "1" -> Bukkit.getWorlds().find { it.environment == World.Environment.NETHER }
            "2" -> Bukkit.getWorlds().find { it.environment == World.Environment.THE_END }
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
