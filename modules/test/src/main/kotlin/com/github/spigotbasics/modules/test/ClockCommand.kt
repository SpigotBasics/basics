package com.github.spigotbasics.modules.test

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

@CommandAlias("clock")
class ClockCommand(private val module: BasicsModule) : BaseCommand() {

    @Default
    fun startClock(sender: CommandSender) {
        module.scheduler.runTimer(20, 20) {
            sender.sendMessage("§a${System.currentTimeMillis()}")
        }
    }

}