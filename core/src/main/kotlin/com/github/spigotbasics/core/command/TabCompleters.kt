package com.github.spigotbasics.core.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import java.util.Collections

object TabCompleters {
    fun getPlayers(
        sender: CommandSender,
        string: String,
    ): MutableList<String> {
        val names = mutableListOf<String>()
        Bukkit.getOnlinePlayers()
            .filter { canSee(sender, it) && StringUtil.startsWithIgnoreCase(it.name, string) }
            .forEach { names.add(it.name) }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER)
        return names
    }

    private fun canSee(
        sender: CommandSender,
        player: Player,
    ): Boolean {
        return if (sender is Player) {
            sender.canSee(player)
        } else {
            true
        }
    }
}
