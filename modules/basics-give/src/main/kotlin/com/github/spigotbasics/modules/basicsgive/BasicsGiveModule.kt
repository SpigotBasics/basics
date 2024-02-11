package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.GiveCommandContext
import com.github.spigotbasics.core.command.parsed.IntArgument
import com.github.spigotbasics.core.command.parsed.MaterialArgument
import com.github.spigotbasics.core.command.parsed.PlayerArgument
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Material
import org.bukkit.entity.Player

class BasicsGiveModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission("basics.give", "Allows the player to use the /give command")


    val giveCommandPathBasic =
        ArgumentPath(
            listOf(PlayerArgument(), MaterialArgument()),
        ) { parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material)
        }

    val giveCommandPathWithAmount =
        ArgumentPath(
            listOf(PlayerArgument(), MaterialArgument(), IntArgument()),
        ) { parsedArgs ->
            GiveContext(parsedArgs[0] as Player, parsedArgs[1] as Material, parsedArgs[2] as Int)
        }


    override fun onEnable() {
        createParsedCommand<GiveContext>("give", permission)
            .paths(listOf(giveCommandPathWithAmount, giveCommandPathBasic))
            .executor(GiveExecutor())
            .register()
    }
}
