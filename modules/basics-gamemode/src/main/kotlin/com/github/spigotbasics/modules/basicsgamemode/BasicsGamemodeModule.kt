package com.github.spigotbasics.modules.basicsgamemode

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.GameMode

class BasicsGamemodeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val msgConfig = getConfig(ConfigName.fromName("messages.yml"))
    val msgChangedOthers get() = msgConfig.getMessage("gamemode-changed-others")
    val msgChangedSelf get() = msgConfig.getMessage("gamemode-changed-self")
    val nameSurvival get() = msgConfig.getMessage("survival")
    val nameCreative get() = msgConfig.getMessage("creative")
    val nameAdventure get() = msgConfig.getMessage("adventure")
    val nameSpectator get() = msgConfig.getMessage("spectator")

    override fun reloadConfig() {
        super.reloadConfig()
        msgConfig.reload()
    }

    override fun onEnable() {
    }

    fun getGameModeName(gameMode: GameMode): Message {
        return when(gameMode) {
            GameMode.SURVIVAL -> nameSurvival
            GameMode.CREATIVE -> nameCreative
            GameMode.ADVENTURE -> nameAdventure
            GameMode.SPECTATOR -> nameSpectator
        }
    }
    
}