package com.github.spigotbasics.modules.test

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable

@CommandAlias("clock")
class ClockCommand(private val module: BasicsModule) : BaseCommand() {

    @Subcommand("basics-scheduler")
    fun basicsScheduler(sender: CommandSender) {
        module.scheduler.runTimer(20, 20) {
            sender.sendMessage("§aBasics Scheduler")
        }
    }

    @Subcommand("bukkit-scheduler")
    fun bukkitScheduler(sender: CommandSender) {
        Bukkit.getScheduler().runTaskTimer(module.plugin, Runnable {
            sender.sendMessage("§cBukkit Scheduler")
        }, 20, 20)
    }

    @Subcommand("bukkit-runnable")
    fun bukkitRunnable(sender: CommandSender) {
        object : BukkitRunnable() {
            override fun run() {
                sender.sendMessage("§cBukkit Runnable")
            }
        }.runTaskTimer(module.plugin, 20, 20)
    }

}
