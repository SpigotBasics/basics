package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Location
import org.bukkit.World

class BasicsWorldModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    //private val playerPositions = mutableMapOf<UUID, LocationList>()
    private val permission = permissionManager.createSimplePermission("basics.world", "Allows to switch worlds using /world")

    override fun onEnable() {
        createCommand("world", permission)
            .usage("/world <world>")
            .description("Teleport to another world")
            .executor(WorldCommand(this))
            .register()
    }

    private fun getCoordFactor(from: World, to: World): Double {
        val fromEnv = from.environment
        val toEnv = to.environment
        return when {
            fromEnv == World.Environment.NETHER && toEnv != World.Environment.NETHER -> 8.0
            fromEnv != World.Environment.NETHER && toEnv == World.Environment.NETHER -> 0.125
            else -> 1.0
        }
    }

    private fun getScaledLocationInOtherWorld(origin: Location, newWorld: World): Location {
        val factor = getCoordFactor(origin.world!!, newWorld)
        return Location(newWorld, origin.x * factor, origin.y + .5, origin.z * factor, origin.yaw, origin.pitch)
    }


}