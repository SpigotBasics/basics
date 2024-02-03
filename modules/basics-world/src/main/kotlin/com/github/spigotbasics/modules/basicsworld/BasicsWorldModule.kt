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




}