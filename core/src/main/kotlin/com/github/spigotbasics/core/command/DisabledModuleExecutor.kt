package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.module.DisabledModule
import org.bukkit.ChatColor

object DisabledModuleExecutor : BasicsCommandExecutor(DisabledModule) {
    override fun execute(context: BasicsCommandContext): Boolean {
        context.sender.sendMessage(ChatColor.RED.toString() + "The module belonging to this command is disabled.")
        return true
    }

}