package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed.AnySender
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.PlayerSender
import com.github.spigotbasics.core.command.parsed.arguments.IntArg
import com.github.spigotbasics.core.command.parsed.arguments.ItemMaterialArg
import com.github.spigotbasics.core.command.parsed.arguments.PlayerArg
import com.github.spigotbasics.core.messages.tags.providers.ItemStackTag
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BasicsGiveModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission("basics.give", "Allows the player to use the /give command")

    val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.give.others",
            "Allows to give items to others using the /give command",
        )

    fun msgGiveOthers(
        receiver: Player,
        item: ItemStack,
    ) = messages.getMessage("give-others")
        .concerns(receiver)
        .tags(ItemStackTag(item))

    fun msgGiveSelf(
        receiver: Player,
        item: ItemStack,
    ) = messages.getMessage("give-self")
        .concerns(receiver)
        .tags(ItemStackTag(item))

    val pathPlayerItem =
        ArgumentPath(
            AnySender,
            listOf(
                PlayerArg("Receiving Player"),
                ItemMaterialArg("Item"),
            ),
            listOf(permissionOthers),
        ) { _, parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material)
        }

    val pathItem =
        ArgumentPath(
            PlayerSender,
            listOf(
                ItemMaterialArg("Item"),
            ),
        ) { sender, parsedArgs ->
            GiveContext(sender as Player, parsedArgs[0] as Material)
        }

    val pathItemAmount =
        ArgumentPath(
            PlayerSender,
            listOf(
                ItemMaterialArg("Item"),
                IntArg("Amount"),
            ),
        ) { sender, parsedArgs ->
            GiveContext(sender as Player, parsedArgs[0] as Material, parsedArgs[1] as Int)
        }

    val pathPlayerItemAmount =
        ArgumentPath(
            AnySender,
            listOf(
                PlayerArg("Receiving Player"),
                ItemMaterialArg("Item"),
                IntArg("Amount"),
            ),
            listOf(permissionOthers),
        ) { _, parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material, parsedArgs[2] as Int)
        }

    override fun onEnable() {
        createParsedCommand<GiveContext>("give", permission)
            .paths(listOf(pathItem, pathPlayerItem, pathItemAmount, pathPlayerItemAmount))
            .executor(GiveExecutor(this))
            .usage("[Receiving Player] <Item> [Amount]")
            .register()

        createCommand("givesnbt", permission)
            .executor { context ->
                val snbt = context.args[0]
                val item = Bukkit.getItemFactory().createItemStack(snbt)
                (context.sender as Player).inventory.addItem(item)
                null
            }
            .register()
    }
}
