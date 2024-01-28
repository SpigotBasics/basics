package com.github.spigotbasics.core.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager

class BasicsCommandManager(private val manager: PaperCommandManager/*plugin: BasicsPlugin*/) {

    private val commands: MutableList<BaseCommand> = ArrayList()
    //private val manager: PaperCommandManager = PaperCommandManager(plugin)
//    private val rootCommands: Field = CommandManager::class.java.getDeclaredField("rootCommands")


    fun registerCommand(command: BaseCommand) {
        commands.add(command)
        manager.registerCommand(command)
    }

    fun unregisterCommand(command: BaseCommand) {
        dispose(command)
        commands.remove(command)
    }

    fun unregisterAll() {
        commands.forEach() { dispose(it) }
        commands.clear()
    }

    private fun dispose(command: BaseCommand) {
        manager.unregisterCommand(command)
        /*
        * The code below, while not currently functioning as intended is supposed to remove left over data ACF
        * Keeps on commands. While this code is effective at removing leaky data in memory from disabled modules it is
        * simply equally as effective currently to only run the above code as it results in the same client
        * communication.
        *
        * Related to https://github.com/aikar/commands/issues/273
         */
//        val root = manager.getRootCommand(command.name) ?: return
//        manager.unregisterCommand(root as BukkitRootCommand) // removes from command map
//        rootCommands.isAccessible = true
//        val roots = rootCommands.get(manager) as HashMap<String, RootCommand>
//        roots.remove(root.name)
//        rootCommands.isAccessible = false
//        println(manager.getRootCommand(command.name))
    }

}
