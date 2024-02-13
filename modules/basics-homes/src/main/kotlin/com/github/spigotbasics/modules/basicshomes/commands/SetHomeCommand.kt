package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.extensions.getPermissionNumberValue
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.data.Home
import org.bukkit.entity.Player

class SetHomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    private val messages = module.messages

    override fun execute(context: RawCommandContext): CommandResult {
        if (context.sender !is Player) {
            return CommandResult.NOT_FROM_CONSOLE
        }

        var homeName = "home"

        if (context.args.size > 1) {
            return CommandResult.USAGE
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

                if (homeList.isEmpty()) {
                    // ... and they haven't set one yet, that's okay.
                } else if (homeList.size == 1 && homeList.getHome(homeName) != null) {
                    // ... and they're replacing their only home, that's okay.
                } else {
                    // ... and they're not replacing that one, that's not okay.
                    messages.homeLimitReached(1)
                        .sendToSender(player)

                    return CommandResult.SUCCESS
                }
            }
        }

        val isOkay =
            when {
                // Player may set unlimited homes
                allowUnlimited -> true

                // Player may set more homes
                homeList.size < maxAllowed -> true

                // Player already set max homes, but is replacing an existing one
                homeList.size == maxAllowed && homeList.getHome(homeName) != null -> true

                // Buy a rank, ffs
                else -> false
            }

        if (!isOkay) {
            messages.homeLimitReached(maxAllowed).sendToSender(player)
            return CommandResult.SUCCESS
        }

        if (!homeName.matches(module.regex.toRegex())) {
            messages.homeInvalidName(module.regex).sendToSender(player)
            return CommandResult.SUCCESS
        }

        val home = Home(homeName, location)

        homeList.addHome(home)
        messages.homeSet(home).sendToSender(player)
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: RawCommandContext): MutableList<String> {
        if (context.sender !is Player) {
            return mutableListOf()
        }
        val player = context.sender as Player
        return module.getHomeList(player.uniqueId).listHomeNames().partialMatches(context.args[0])
    }
}
