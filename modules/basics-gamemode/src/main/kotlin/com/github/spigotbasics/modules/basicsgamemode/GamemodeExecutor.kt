package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.command.common.BasicsCommandExecutor
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class GamemodeExecutor(val module: BasicsGamemodeModule) : BasicsCommandExecutor(module) {
    override fun execute(context: RawCommandContext): CommandResult {
        val args = context.args
        val sender = context.sender
        var target: Player

        if (args.isEmpty() || args.size > 2) {
            return CommandResult.USAGE
        }

        val gameModeName = args[0]
        val gameMode = module.toGameMode(gameModeName)
        if (gameMode == null) {
            return failInvalidArgument(gameModeName)
        }

        requirePermission(sender, module.getPermission(gameMode))

        if (args.size == 1) {
            target = requirePlayerOrMustSpecifyPlayerFromConsole(sender)
        } else if (args.size == 2) {
            requirePermission(sender, module.permOthers)
            target = requirePlayer(args[1])
        } else {
            return CommandResult.USAGE
        }

        target.gameMode = gameMode
        val message = if (target == sender) module.msgChangedSelf else module.msgChangedOthers

        message.tagMessage("new-gamemode", module.getName(gameMode))
            .concerns(target)

        message.sendToSender(sender)
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: RawCommandContext): MutableList<String>? {
        if (context.args.size == 1) {
            return StringUtil.copyPartialMatches(
                context.args[0],
                getAllowedGameModeNames(context.sender),
                mutableListOf(),
            )
        }

        if (context.args.size == 2) {
            if (context.sender.hasPermission(module.permOthers)) {
                return null // null = normal list of players
            }
        }

        return mutableListOf()
    }

    private fun getAllowedGameModeNames(sender: CommandSender): MutableList<String> {
        val list = mutableListOf<String>()

        if (sender.hasPermission(module.permSurvival)) {
            list += "survival"
        }
        if (sender.hasPermission(module.permCreative)) {
            list += "creative"
        }
        if (sender.hasPermission(module.permAdventure)) {
            list += "adventure"
        }
        if (sender.hasPermission(module.permSpectator)) {
            list += "spectator"
        }

        return list
    }
}
