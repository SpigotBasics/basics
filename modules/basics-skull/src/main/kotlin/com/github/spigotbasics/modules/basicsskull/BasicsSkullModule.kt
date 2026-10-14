package com.github.spigotbasics.modules.basicsskull

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.AnyStringArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.extensions.addOrDrop
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class BasicsSkullModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.skull",
            "Allows the player to get their own head",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.skull.others",
            "Allows the player to get the head of any player",
        )

    private fun msgSkull(owner: String) = messages.getMessage("skull").tagUnparsed("owner", owner)

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("skull", permission).mapContext {
            usage = "[name]"
            description("Gives you a player head")
            aliases(listOf("head"))

            path {
                playerOnly()
            }

            path {
                playerOnly()
                permissions(permissionOthers)
                arguments {
                    named("name", AnyStringArg("name"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val name = context["name"] as String?
                    if (name == null) {
                        giveSkull(player, player)
                        return
                    }
                    // Looking up an unknown name may block on a Mojang request
                    scheduler.runTaskAsync {
                        val owner = getOfflinePlayer(name)
                        scheduler.runTask { if (player.isOnline) giveSkull(player, owner) }
                    }
                }
            },
        ).register()
    }

    private fun giveSkull(
        player: Player,
        owner: OfflinePlayer,
    ) {
        val skull = ItemStack(Material.PLAYER_HEAD)
        val meta = skull.itemMeta as SkullMeta
        meta.owningPlayer = owner
        skull.itemMeta = meta

        skull.addOrDrop(player.inventory)
        msgSkull(owner.name ?: "?").sendToPlayer(player)
    }

    @Suppress("DEPRECATION") // Looking up by name is exactly what we want here
    private fun getOfflinePlayer(name: String): OfflinePlayer = server.getOfflinePlayer(name)
}
