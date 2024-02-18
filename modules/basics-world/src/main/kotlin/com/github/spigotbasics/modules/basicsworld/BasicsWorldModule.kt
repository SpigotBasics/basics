package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.command.parsed.arguments.StringOptionArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.World
import org.bukkit.permissions.Permission

class BasicsWorldModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission("basics.world", "Allows to switch worlds using /world")
    val worldPermissions = mutableMapOf<String, Permission>()

    fun msgAlreadyInWorld(world: String) = messages.getMessage("already-in-world").tagUnparsed("world", world)

    fun msgSuccess(world: String) = messages.getMessage("world-teleported").tagUnparsed("world", world)

    fun msgUnsuccessful(world: String) = messages.getMessage("teleport-unsuccessful").tagUnparsed("world", world)

    fun msgStartingTeleport(world: String) =
        messages.getMessage(
            "starting-teleport",
        ).tagUnparsed("world", world)

    override fun onEnable() {
        val worldArg = WorldArg(this, "World")
        val forceArg = StringOptionArg("Force", listOf("--force", "-f"))
        commandFactory.parsedCommandBuilder("world", permission)
            .mapContext {
                usage = "[world]"
                aliases(listOf("worldtp", "tpworld"))

                description("Teleport to another world")

                path {
                    playerOnly()
                    arguments {
                        named("world", worldArg)
                    }
                }

                path {
                    playerOnly()
                    arguments {
                        named("world", worldArg)
                        named("force", forceArg)
                    }
                }
            }.executor(BasicsWorldCommand(this)).register()

        server.worlds.forEach {
            getWorldPermission(it)
        }
    }

    override fun reloadConfig() {
        config.reload()
        messages.reload()
    }

    fun getWorldPermission(world: World): Permission {
        val name = world.name
        return worldPermissions.computeIfAbsent(name.lowercase()) {
            permissionManager.createSimplePermission(
                "basics.world.${name.lowercase()}",
                "Allows to switch to world '$name' using /world $name",
            )
        }
    }
}
