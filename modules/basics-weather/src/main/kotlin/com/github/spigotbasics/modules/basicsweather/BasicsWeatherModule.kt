package com.github.spigotbasics.modules.basicsweather

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.WeatherType

class BasicsWeatherModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val perm = permissionManager.createSimplePermission("basics.weather", "Allows the user to change the weather")

    val msgClear get() = messages.getMessage("clear")
    val msgStorm get() = messages.getMessage("storm")
    val msgWeatherChanged get() = messages.getMessage("weather-changed")

    override fun onEnable() {
        createCommand("weather", perm)
            .usage("<clear|storm> [duration]")
            .executor(WeatherCommand(this))
            .register()
    }

    fun weatherTypeFromString(str: String): WeatherType? {
        return when (str.lowercase()) {
            "sun", "clear", "sunny" -> WeatherType.CLEAR
            "storm", "rain", "rainy", "stormy" -> WeatherType.DOWNFALL
            else -> null
        }
    }
}
