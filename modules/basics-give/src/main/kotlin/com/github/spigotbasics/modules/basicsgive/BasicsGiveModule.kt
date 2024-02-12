package com.github.spigotbasics.modules.basicsgive

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
    ) = messages.getMessage("give-others").concerns(receiver).tags(ItemStackTag(item))

    fun msgGiveSelf(
        receiver: Player,
        item: ItemStack,
    ) = messages.getMessage("give").concerns(receiver).tags(ItemStackTag(item))

    val pathPlayerItem =
        createArgumentPath<GiveContext>(
            PlayerArg("Receiving Player"),
            ItemMaterialArg("Item"),
        )
            .permissions(permissionOthers)
            .contextBuilder { _, args ->
                GiveContext(args[0] as Player, args[1] as Material)
            }.build()

    val pathItem =
        createArgumentPath<GiveContext>(
            ItemMaterialArg("Item"),
        )
            .playerOnly()
            .contextBuilder { sender, args ->
                GiveContext(sender as Player, args[0] as Material)
            }.build()

    val pathItemAmount =
        createArgumentPath<GiveContext>(
            ItemMaterialArg("Item"),
            IntArg("Amount"),
        )
            .playerOnly()
            .contextBuilder { sender, parsedArgs ->
                GiveContext(sender as Player, parsedArgs[0] as Material, parsedArgs[1] as Int)
            }.build()

    val pathPlayerItemAmount =
        createArgumentPath<GiveContext>(
            PlayerArg("Receiving Player"),
            ItemMaterialArg("Item"),
            IntArg("Amount"),
        )
            .permissions(permissionOthers)
            .contextBuilder { _, parsedArgs ->
                GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material, parsedArgs[2] as Int)
            }.build()

    override fun onEnable() {
        createParsedCommand<GiveContext>("give", permission).paths(
            pathItem,
            pathPlayerItem,
            pathItemAmount,
            pathPlayerItemAmount,
        ).executor(GiveExecutor(this)).usage("[Receiving Player] <Item> [Amount]").register()

        createCommand("givesnbt", permission).executor { context ->
            val snbt = context.args[0]
            val item = Bukkit.getItemFactory().createItemStack(snbt)
            (context.sender as Player).inventory.addItem(item)
            null
        }.register()
    }
}
