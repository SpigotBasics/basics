package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.AnySender
import com.github.spigotbasics.core.command.parsed.PlayerSender
import com.github.spigotbasics.core.command.parsed.arguments.IntArgument
import com.github.spigotbasics.core.command.parsed.arguments.MaterialArgument
import com.github.spigotbasics.core.command.parsed.arguments.PlayerArgument
import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BasicsGiveModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission("basics.give", "Allows the player to use the /give command")

    fun msgGiveOthers(
        receiver: Player,
        item: ItemStack,
    ) = messages.getMessage("give-others").concerns(receiver).tagParsed("item", item.type.name.toHumanReadable())
        .tagParsed("amount", item.amount.toString())

    val pathPlayerItem =
        ArgumentPath(
            AnySender,
            listOf(
                PlayerArgument("Receiving Player"),
                MaterialArgument("Item"),
            ),
        ) { _, parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material)
        }

    val pathItem =
        ArgumentPath(
            PlayerSender,
            listOf(
                MaterialArgument("Item"),
            ),
        ) { sender, parsedArgs ->
            GiveContext(sender as Player, parsedArgs[0] as Material)
        }

    val pathItemAmount =
        ArgumentPath(
            PlayerSender,
            listOf(
                MaterialArgument("Item"),
                IntArgument("Amount"),
            ),
        ) { sender, parsedArgs ->
            GiveContext(sender as Player, parsedArgs[0] as Material, parsedArgs[1] as Int)
        }

    val pathPlayerItemAmount =
        ArgumentPath(
            AnySender,
            listOf(
                PlayerArgument("Receiving Player"),
                MaterialArgument("Item"),
                IntArgument("Amount"),
            ),
        ) { _, parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material, parsedArgs[2] as Int)
        }

    override fun onEnable() {
        createParsedCommand<GiveContext>("give", permission)
            .paths(listOf(pathItem, pathPlayerItem, pathItemAmount, pathPlayerItemAmount))
            .executor(GiveExecutor(this))
            .register()
    }
}
