package com.github.spigotbasics.modules.basicsspeed

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.FloatRangeArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.arguments.StringOptionArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.extensions.toShortString
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsSpeedModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.speed",
            "Allows the player to change their walk and fly speed",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.speed.others",
            "Allows the player to change the walk and fly speed of other players",
        )

    private fun msgSpeedSet(
        target: Player,
        type: String,
        speed: Float,
        self: Boolean,
    ) = messages.getMessage(if (self) "speed-set" else "speed-set-others")
        .concerns(target)
        .tagUnparsed("type", type)
        .tagParsed("speed", speed.toShortString())

    override fun onEnable() {
        val typeArg = StringOptionArg("type", listOf("walk", "fly"))
        val speedArg = FloatRangeArg("speed", { MIN_SPEED }, { MAX_SPEED })
        val playerArg = SelectorSinglePlayerArg("player")

        commandFactory.parsedCommandBuilder("speed", permission).mapContext {
            usage = "[walk|fly] <speed> [player]"
            description("Changes your walk or fly speed")

            path {
                playerOnly()
                arguments {
                    named("speed", speedArg)
                }
            }

            path {
                playerOnly()
                arguments {
                    named("type", typeArg)
                    named("speed", speedArg)
                }
            }

            path {
                permissions(permissionOthers)
                arguments {
                    named("speed", speedArg)
                    named("player", playerArg)
                }
            }

            path {
                permissions(permissionOthers)
                arguments {
                    named("type", typeArg)
                    named("speed", speedArg)
                    named("player", playerArg)
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val target = context["player"] as Player? ?: sender as Player
                    val type = context["type"] as String? ?: if (target.isFlying) "fly" else "walk"
                    val speed = context["speed"] as Float

                    if (type == "fly") {
                        target.flySpeed = toBukkitSpeed(speed, DEFAULT_FLY_SPEED)
                    } else {
                        target.walkSpeed = toBukkitSpeed(speed, DEFAULT_WALK_SPEED)
                    }
                    msgSpeedSet(target, type, speed, sender == target).sendToSender(sender)
                }
            },
        ).register()
    }

    /** Maps 0-10 to Bukkit's 0.0-1.0, where 1 is the vanilla default */
    private fun toBukkitSpeed(
        speed: Float,
        default: Float,
    ): Float = if (speed < 1) speed * default else default + (speed - 1) / (MAX_SPEED - 1) * (1f - default)

    companion object {
        private const val DEFAULT_WALK_SPEED = 0.2f
        private const val DEFAULT_FLY_SPEED = 0.1f
        private const val MIN_SPEED = 0f
        private const val MAX_SPEED = 10f
    }
}
