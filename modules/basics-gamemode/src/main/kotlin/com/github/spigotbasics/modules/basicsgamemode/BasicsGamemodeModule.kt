package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

class BasicsGamemodeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val msgChangedOthers get() = messages.getMessage("gamemode-changed-others")
    private val msgChangedSelf get() = messages.getMessage("gamemode-changed-self")
    private val nameSurvival get() = messages.getMessage("survival")
    private val nameCreative get() = messages.getMessage("creative")
    private val nameAdventure get() = messages.getMessage("adventure")
    private val nameSpectator get() = messages.getMessage("spectator")

    private val perm =
        permissionManager.createSimplePermission("basics.gamemode", "Allows the player to change their game mode")
    val permSurvival =
        permissionManager.createSimplePermission(
            "basics.gamemode.survival",
            "Allows the player to change their game mode to survival",
        )
    val permCreative =
        permissionManager.createSimplePermission(
            "basics.gamemode.creative",
            "Allows the player to change their game mode to creative",
        )
    val permAdventure =
        permissionManager.createSimplePermission(
            "basics.gamemode.adventure",
            "Allows the player to change their game mode to adventure",
        )
    val permSpectator =
        permissionManager.createSimplePermission(
            "basics.gamemode.spectator",
            "Allows the player to change their game mode to spectator",
        )

    private val permOthers =
        permissionManager.createSimplePermission(
            "basics.gamemode.others",
            "Allows the player to change other players' game modes",
        )

    override fun onEnable() {
        val instance = this
        commandFactory.parsedCommandBuilder("gamemode", perm).mapContext {
            description("Changes the player's game mode")
            usage = "<mode> [player]"
            path {
                playerOnly()
                arguments {
                    named("gamemode", GameModeArgument(instance, "gamemode"))
                }
            }

            path {
                permissions(permOthers)
                arguments {
                    named("gamemode", GameModeArgument(instance, "gamemode"))
                    named("target", SelectorSinglePlayerArg("target"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val gameMode = context["gamemode"] as GameMode
                    val target = if (context["target"] == null) sender as Player else context["target"] as Player

                    val permission = getPermission(gameMode)
                    if (!sender.hasPermission(permission)) {
                        coreMessages.noPermission(permission).concerns(target).sendToSender(sender)
                        return
                    }

                    target.gameMode = gameMode
                    val message = if (target == sender) msgChangedSelf else msgChangedOthers
                    message.tagMessage("new-gamemode", getName(gameMode))
                        .concerns(target)
                        .sendToSender(sender)
                }
            },
        ).register()
    }

    fun getPermission(gameMode: GameMode): Permission {
        return when (gameMode) {
            GameMode.SURVIVAL -> permSurvival
            GameMode.CREATIVE -> permCreative
            GameMode.ADVENTURE -> permAdventure
            GameMode.SPECTATOR -> permSpectator
            else -> throw IllegalArgumentException("Unknown game mode: $gameMode")
        }
    }

    fun getName(gameMode: GameMode): Message {
        return when (gameMode) {
            GameMode.SURVIVAL -> nameSurvival
            GameMode.CREATIVE -> nameCreative
            GameMode.ADVENTURE -> nameAdventure
            GameMode.SPECTATOR -> nameSpectator
            else -> throw IllegalArgumentException("Unknown game mode: $gameMode")
        }
    }
}
