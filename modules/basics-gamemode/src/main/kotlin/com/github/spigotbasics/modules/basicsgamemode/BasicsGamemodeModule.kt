package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import net.kyori.adventure.text.Component
import org.bukkit.GameMode

class BasicsGamemodeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val msgConfig = getConfig(ConfigName.MESSAGES)
    val msgChangedOthers get() = msgConfig.getMessage("gamemode-changed-others")
    val msgChangedSelf get() = msgConfig.getMessage("gamemode-changed-self")
    val nameSurvival get() = msgConfig.getMessage("survival").toComponent()
    val nameCreative get() = msgConfig.getMessage("creative").toComponent()
    val nameAdventure get() = msgConfig.getMessage("adventure").toComponent()
    val nameSpectator get() = msgConfig.getMessage("spectator").toComponent()

    override fun reloadConfig() {
        super.reloadConfig()
        msgConfig.reload()
    }

    override fun onEnable() {
        commandManager.registerCommand(GamemodeCommand(this))
    }

    fun getGameModeName(gameMode: GameMode): Component {
        return when(gameMode) {
            GameMode.SURVIVAL -> nameSurvival
            GameMode.CREATIVE -> nameCreative
            GameMode.ADVENTURE -> nameAdventure
            GameMode.SPECTATOR -> nameSpectator
        }
    }
    
}