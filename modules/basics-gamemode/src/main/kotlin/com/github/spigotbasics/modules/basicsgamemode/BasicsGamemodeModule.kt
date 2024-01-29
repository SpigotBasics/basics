package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.GameMode
import org.bukkit.permissions.Permission

class BasicsGamemodeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val msgConfig = getConfig(ConfigName.fromName("messages.yml"))
    val msgChangedOthers get() = msgConfig.getMessage("gamemode-changed-others")
    val msgChangedSelf get() = msgConfig.getMessage("gamemode-changed-self")
    val nameSurvival get() = msgConfig.getMessage("survival")
    val nameCreative get() = msgConfig.getMessage("creative")
    val nameAdventure get() = msgConfig.getMessage("adventure")
    val nameSpectator get() = msgConfig.getMessage("spectator")

    val perm = permissionManager.createSimplePermission("basics.gamemode", "Allows the player to change their game mode")

    val permSurvival = permissionManager.createSimplePermission("basics.gamemode.survival", "Allows the player to change their game mode to survival")
    val permCreative = permissionManager.createSimplePermission("basics.gamemode.creative", "Allows the player to change their game mode to creative")
    val permAdventure = permissionManager.createSimplePermission("basics.gamemode.adventure", "Allows the player to change their game mode to adventure")
    val permSpectator = permissionManager.createSimplePermission("basics.gamemode.spectator", "Allows the player to change their game mode to spectator")

    val permOthers = permissionManager.createSimplePermission("basics.gamemode.others", "Allows the player to change other players' game modes")

    override fun reloadConfig() {
        super.reloadConfig()
        msgConfig.reload()
    }

    override fun onEnable() {
        createCommand()
            .name("gamemode")
            .permission(perm)
            .description("Changes the player's game mode")
            .usage("/gamemode <mode> [player]")
            .executor(GamemodeExecutor(this))
            .register()
    }

    fun toGameMode(input: String): GameMode? {
        return when(input) {
            "survival", "s", "0" -> GameMode.SURVIVAL
            "creative", "c", "1" -> GameMode.CREATIVE
            "adventure", "a", "2" -> GameMode.ADVENTURE
            "spectator", "sp", "3" -> GameMode.SPECTATOR
            else -> null
        }
    }

    fun getPermission(gameMode: GameMode): Permission {
        return when(gameMode) {
            GameMode.SURVIVAL -> permSurvival
            GameMode.CREATIVE -> permCreative
            GameMode.ADVENTURE -> permAdventure
            GameMode.SPECTATOR -> permSpectator
            else -> throw IllegalArgumentException("Unknown game mode: $gameMode")
        }
    }

    fun getName(gameMode: GameMode): Message {
        return when(gameMode) {
            GameMode.SURVIVAL -> nameSurvival
            GameMode.CREATIVE -> nameCreative
            GameMode.ADVENTURE -> nameAdventure
            GameMode.SPECTATOR -> nameSpectator
            else -> throw IllegalArgumentException("Unknown game mode: $gameMode")
        }
    }
    
}