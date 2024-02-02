package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.getPermissionNumberValue
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.data.Home
import org.bukkit.entity.Player

class SetHomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        if (context.sender !is Player) {
            module.plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return true
        }

        var homeName = "home"

        if (context.args.size > 1) {
            return false
        }

        val player = context.sender as Player
        val location = player.location

        val homeList = module.getHomeList(player.uniqueId)
        val maxAllowed = player.getPermissionNumberValue(module.permissionSetHomeMultiple.name) ?: 2
        val allowUnlimited = player.hasPermission(module.permissionSetHomeUnlimited)

        if (context.args.size == 1) {

            homeName = context.args[0]

            if (!player.hasPermission(module.permissionSetHomeMultiple)) {
                // Player can only set one home ...

                if(homeList.isEmpty()) {
                    // ... and they haven't set one yet, that's okay.

                } else if (homeList.size == 1 && homeList.getHome(homeName) != null) {
                    // ... and they're replacing their only home, that's okay.

                } else {
                    // ... and they're not replacing that one, that's not okay.
                    module.msgHomeLimitReached(1)
                        .sendToSender(player)

                    return true
                }
            }

        }

        val isOkay = when {
            allowUnlimited -> true // Player may set unlimited homes
            homeList.size < maxAllowed -> true // Player may set more homes
            homeList.size == maxAllowed && homeList.getHome(homeName) != null -> true // Player already set max homes, but is replacing an existing one
            else -> false // Buy a rank, ffs
        }

        if (!isOkay) {
            module.msgHomeLimitReached(maxAllowed).sendToSender(player)
            return true
        }

        val home = Home(homeName, location)

        homeList.addHome(home)
        module.msgHomeSet(home).sendToSender(player)
        return true
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        if (context.sender !is Player) {
            return mutableListOf()
        }
        val player = context.sender as Player
        return module.getHomeList(player.uniqueId).listHomes().partialMatches(context.args[0])
    }
}