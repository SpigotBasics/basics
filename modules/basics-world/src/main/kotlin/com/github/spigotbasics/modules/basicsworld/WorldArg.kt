package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.util.WorldUtils
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permissible

class WorldArg(private val module: BasicsWorldModule, name: String) : CommandArgument<World>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): World? {
        val world = getWorld(value) ?: return null
        if (!sender.hasPermission(module.getWorldPermission(world))) {
            return null
        }
        return world
    }

    private fun getWorld(name: String): World? {
        val worlds = Bukkit.getWorlds()
        return when (name) {
            "0" -> worlds[0]
            "1" -> worlds.find { it.environment == World.Environment.NETHER }
            "2" -> worlds.find { it.environment == World.Environment.THE_END }
            else -> Bukkit.getWorld(name)
        }
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        val world = getWorld(value)
        if (world == null) {
            return Basics.messages.worldNotFound(value)
        }
        val perm = module.getWorldPermission(world)
        if (!sender.hasPermission(perm)) {
            return Basics.messages.noPermission(perm)
        }
        throw IllegalStateException()
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return allWorldsAnd012(sender)
    }

    private fun allWorldsAnd012(sender: Permissible): MutableList<String> {
        val list =
            Bukkit.getWorlds().filter {
                val hasPerm = sender.hasPermission(module.getWorldPermission(it))
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
