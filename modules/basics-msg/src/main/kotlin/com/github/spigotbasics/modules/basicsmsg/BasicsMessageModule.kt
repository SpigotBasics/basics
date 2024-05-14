package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.core.command.parsed.arguments.GreedyStringArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.modules.basicsmsg.command.CommandMessage
import com.github.spigotbasics.modules.basicsmsg.command.CommandRespond
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.PermissionDefault

class BasicsMessageModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permissionPM =
        permissionManager.createSimplePermission(
            "basics.msg",
            "Allows the player to send private messages",
            PermissionDefault.TRUE,
        )
    private val permissionRespond =
        permissionManager.createSimplePermission(
            "basics.respond",
            "Allows the player a shortcut to respond to private messages",
            PermissionDefault.TRUE,
        )

    val lastMessagedStore = LastMessagedStore()

    override val messages: Messages = getConfig(ConfigName.MESSAGES, Messages::class.java)

    override fun onEnable() {
        eventBus.subscribe(PlayerJoinEvent::class.java) { lastMessagedStore.forgetLastMessagedUUID(it.player.uniqueId) }
        commandFactory.parsedCommandBuilder("message", permissionPM).mapContext {
            description("Sends a private message to another player")
            usage = "<player> <message>"
            aliases(listOf("msg", "tell", "whisper"))

            path {
                arguments {
                    named("player", SelectorSinglePlayerArg("player"))
                    named("message", GreedyStringArg("message"))
                }
            }
        }.executor(CommandMessage(messages, lastMessagedStore)).register()

        commandFactory.parsedCommandBuilder("respond", permissionRespond).mapContext {
            description("Responds to the person the last message was sent to in this session")
            usage = "<message>"
            aliases(listOf("r", "re"))

            path {
                playerOnly()
                arguments {
                    named("message", GreedyStringArg("message"))
                }
            }
        }.executor(CommandRespond(messages, lastMessagedStore)).register()
    }
}
