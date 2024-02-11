//package com.github.spigotbasics.core.command.parsed
//
//import org.bukkit.Material
//import org.bukkit.entity.Player
//
//val giveCommandPathBasic =
//    ArgumentPath<GiveCommandContext>(
//        listOf(PlayerArgument(), MaterialArgument()),
//    ) { parsedArgs ->
//        GiveCommandContext(parsedArgs[0] as Player, parsedArgs[1] as Material)
//    }
//
//// Path for player, material, and amount
//val giveCommandPathWithAmount =
//    ArgumentPath<GiveCommandContext>(
//        listOf(PlayerArgument(), MaterialArgument(), IntArgument()),
//    ) { parsedArgs ->
//        GiveCommandContext(parsedArgs[0] as Player, parsedArgs[1] as Material, parsedArgs[2] as Int)
//    }
//
//class GiveCommand(private val paths: List<ArgumentPath<GiveCommandContext>>) {
//    fun execute(input: List<String>): GiveCommandContext? {
//        for (path in paths) {
//            path.parse(input)?.let { return it }
//        }
//        return null // No matching path found
//    }
//}
//
//val giveCommand = GiveCommand(listOf(giveCommandPathWithAmount, giveCommandPathBasic))
