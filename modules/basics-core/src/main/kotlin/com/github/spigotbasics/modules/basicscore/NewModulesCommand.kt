package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.MapCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContextExecutor
import com.github.spigotbasics.core.command.parsed.ParsedCommandExecutor
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.command.CommandSender

class NewModulesCommand(private val moduleManager: ModuleManager, private val messageFactory: MessageFactory) : ParsedCommandContextExecutor<MapCommandContext> {

    val msgHelp =
        messageFactory.createMessage(
            """
            <gold>Basics Modules Help</gold>
            <gray>---------------------------------------------------------------------</gray>
            <gold><click:run_command:/module list>/module list</click></gold> - <gray>Show all loaded modules</gray>
            <gold><click:suggest_command:/module info >/module info <module></click></gold> - <gray>Show info about a module</gray>
            <gold><click:suggest_command:/module enable >/module enable <module></click></gold> - <gray>Enable a module</gray>
            <gold><click:suggest_command:/module disable >/module disable <module></click></gold> - <gray>Disable a module</gray>
            <gold><click:suggest_command:/module reload >/module reload <module></click></gold> - <gray>Reload a module's config</gray>
            <gold><click:suggest_command:/module unload >/module unload <module></click></gold> - <gray>Unload a module</gray>
            <gold><click:suggest_command:/module load >/module load <file></click></gold> - <gray>Load a module</gray>
            <gold><click:suggest_command:/module reloadjar >/module reloadjar <module></click></gold> - <gray>Reload a module's jar</gray>
            """.trimIndent(),
        )

    val msgNotImplemented = messageFactory.createMessage("<red>Not implemented yet :')</red>")

    override fun execute(sender: CommandSender, context: MapCommandContext) {
        val sub = context.getOrDefault("sub", "help") as String
        when (sub) {
            "list" -> {
                sender.sendMessage("List of modules")
            }
            else -> {
                sender.sendMessage("Unknown subcommand")
            }
        }
    }
}