package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.parsed.arguments.SelectorMultiEntityArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSingleEntityArg
import com.github.spigotbasics.core.command.parsed.arguments.TripleContextCoordinatesArg
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.tags.providers.LocationTag
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Location
import org.bukkit.entity.Entity

class BasicsTpModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission = permissionManager.createSimplePermission("basics.tp", "Allows teleporting")
    private val permissionOthers =
        permissionManager.createSimplePermission("basics.tp.others", "Allows teleporting other players")

    private val msgTeleported get() = messages.getMessage("teleported")
    private val coordinates get() = messages.getMessage("coordinates")

    fun createMessage(
        teleported: Entity,
        destination: Either<Location, Entity>,
    ): Message {
        val msg = msgTeleported
        if (destination is Either.Left) {
            val coordinates = coordinates
            coordinates.tags(LocationTag(destination.value, 1))
            msg.tagMessage("destination", coordinates)
        } else {
            msg.tagUnparsed("destination", destination.rightOrNull()?.name ?: "null")
        }
        msg.tagUnparsed("target", teleported.name)
        return msg
    }

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("tp", permission).mapContext {
            usage = "[player] <x y z [yaw pitch]| entity>"

            // x y z
            path {
                playerOnly()
                arguments {
                    named("destination", TripleContextCoordinatesArg("Destination Coordinates"))
                }
            }

            // @entity
            path {
                playerOnly()
                arguments {
                    named("destination", SelectorSingleEntityArg("Destination Entity"))
                }
            }

            // @entities @entity
            path {
                arguments {
                    named("targets", SelectorMultiEntityArg("Entities to teleport"))
                    named("destination", SelectorSingleEntityArg("Destination Entity"))
                }
                permissions(permissionOthers)
            }

            // @entities x y z
            path {
                arguments {
                    named("targets", SelectorMultiEntityArg("Entities to teleport"))
                    named("destination", TripleContextCoordinatesArg("Destination Coordinates"))
                }
                permissions(permissionOthers)
            }

            executor(TeleportCommand(this@BasicsTpModule))
        }.register()
    }
}
