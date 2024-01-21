package com.github.spigotbasics.modules.test

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ForbiddenFruitException
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@CommandAlias("clock")
class ClockCommand(private val module: BasicsModule) : BaseCommand() {

    @Default
    fun startClock(sender: CommandSender) {
        module.scheduler.runTimer(20, 20) {
            sender.sendMessage("§a${System.currentTimeMillis()}")
        }
    }

    @Subcommand("tryscheduler")
    fun tryScheduler() {
        try {
            Bukkit.getScheduler().runTaskLater(module.plugin, Runnable {
                Bukkit.broadcastMessage("§cI was able to access the scheduler :< :< :<")
            }, 5)
        } catch (e: ForbiddenFruitException) {
            Bukkit.broadcastMessage("§aI was not able to access the scheduler :3")
        }
    }
}
