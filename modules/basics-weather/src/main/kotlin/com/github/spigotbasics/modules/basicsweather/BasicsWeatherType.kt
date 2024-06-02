package com.github.spigotbasics.modules.basicsweather

import org.bukkit.World

enum class BasicsWeatherType(val activate: (World, Int?) -> Unit) {
    CLEAR({ world: World, duration: Int? ->
        if (!world.isClearWeather) world.setStorm(false)
        if (duration != null) world.clearWeatherDuration = duration
    }),
    RAIN({ world: World, duration: Int? ->
        if (!world.hasStorm()) world.setStorm(true)
        if (duration != null) world.weatherDuration = duration
    }),
    THUNDER({ world: World, duration: Int? ->
        RAIN.activate(world, duration)
        if (!world.isThundering) world.isThundering = true
        if (duration != null) world.thunderDuration = duration
    }),
    ;

    companion object {
        fun fromString(weatherType: String): BasicsWeatherType? {
            return if (weatherType.equals("clear", true)) {
                CLEAR
            } else if (weatherType.equals("rain", true)) {
                RAIN
            } else if (weatherType.equals("thunder", true)) {
                THUNDER
            } else {
                null
            }
        }
    }
}
