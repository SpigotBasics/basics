package com.github.spigotbasics.modules.basicsseen

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.AnyStringArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

class BasicsSeenModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.seen",
            "Allows the player to see when another player was last online",
        )

    private fun msgOnline(name: String) = messages.getMessage("seen-online").tagUnparsed("name", name)

    private fun msgOffline(
        name: String,
        time: String,
    ) = messages.getMessage("seen-offline").tagUnparsed("name", name).tagUnparsed("time", time)

    private fun msgNever(name: String) = messages.getMessage("seen-never").tagUnparsed("name", name)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("seen", permission).mapContext {
            usage = "<name>"
            description("Shows when a player was last online")

            path {
                arguments {
                    named("name", AnyStringArg("name"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val name = context["name"] as String
                    val online = server.getPlayerExact(name)
                    if (online != null) {
                        msgOnline(online.name).sendToSender(sender)
                        return
                    }

                    // Core keeps a name -> UUID record of every player that joined, so this never asks Mojang
                    plugin.corePlayerData.getUuidForName(name).thenAccept { uuid ->
                        scheduler.runTask {
                            val lastPlayed = uuid?.let { server.getOfflinePlayer(it).lastPlayed } ?: 0L
                            if (lastPlayed == 0L) {
                                msgNever(name).sendToSender(sender)
                            } else {
                                msgOffline(name, formatDuration(System.currentTimeMillis() - lastPlayed)).sendToSender(sender)
                            }
                        }
                    }
                }
            },
        ).register()
    }

    private fun formatDuration(millis: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

        val parts = mutableListOf<String>()
        if (days > 0) parts += "${days}d"
        if (hours > 0) parts += "${hours}h"
        if (minutes > 0) parts += "${minutes}m"
        if (parts.isEmpty()) parts += "${seconds}s"
        return parts.joinToString(" ")
    }
}
