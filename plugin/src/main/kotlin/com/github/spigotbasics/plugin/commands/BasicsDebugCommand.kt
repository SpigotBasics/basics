package com.github.spigotbasics.plugin.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.Either
//import com.github.spigotbasics.core.facade.entity.PlayerGetDisplayNameAccess
import io.papermc.lib.PaperLib
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("basicsdebug|bdebug|bd")
class BasicsDebugCommand(private val plugin: BasicsPlugin): BaseCommand() {

    @Subcommand("classforname")
    fun classForName(sender: CommandSender, className: String) {
        try {
            val clazz = Class.forName(className)
            sender.sendMessage("§a${clazz.name} §bfrom classloader §a${clazz.classLoader}")
        } catch (e: Throwable) {
            sender.sendMessage("§c${e.javaClass.name}")
        }
    }

//    @Subcommand("displayname")
//    fun displayNameSelf(player: Player) {
//        displayNameOther(player, player)
//    }

//    @Subcommand("displayname")
//    private fun displayNameOther(sender: CommandSender, player: Player) {
//        val result = PlayerGetDisplayNameAccess.get(player)
//        when(result) {
//            is Either.Left -> sender.sendMessage("§aIt's a component: ${result.value}")
//            is Either.Right -> sender.sendMessage("§cIt's a String: ${result.value}")
//        }
//        if(PaperLib.isPaper()) {
//            if(result is Either.Right) {
//                error("Expected Either.Left / Component on Paper, got Either.Right / String")
//            }
//        } else {
//            if(result is Either.Left) {
//                error("Expected Either.Right / String on Spigot, got Either.Left / Component")
//            }
//        }
//    }

}