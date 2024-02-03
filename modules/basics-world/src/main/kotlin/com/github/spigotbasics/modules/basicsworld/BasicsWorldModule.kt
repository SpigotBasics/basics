package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Location
import org.bukkit.World

class BasicsWorldModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    //private val playerPositions = mutableMapOf<UUID, LocationList>()
    private val permission = permissionManager.createSimplePermission("basics.world", "Allows to switch worlds using /world")

    private val messages = getConfig(ConfigName.MESSAGES)

    fun msgAlreadyInWorld(world: String) = messages.getMessage("already-in-world").tagUnparsed("world", world)
    fun msgSuccess(world: String) = messages.getMessage("world-teleported").tagUnparsed("world", world)
    fun msgUnsuccessful(world: String) = messages.getMessage("teleport-unsuccessful").tagUnparsed("world", world)
    fun msgStartingTeleport(world: String) = messages.getMessage("starting-teleport").tagUnparsed("world", world)

    override fun onEnable() {
        createCommand("world", permission)
            .usage("/world <world>")
            .description("Teleport to another world")
            .executor(WorldCommand(this))
            .register()
    }

    override fun reloadConfig() {
        super.reloadConfig()
        messages.reload()
    }




}