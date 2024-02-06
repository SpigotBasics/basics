package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandException
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.extensions.addAnd
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.command.CommandSender

class ModulesCommand(val module: BasicsCoreModule) : BasicsCommandExecutor(module) {

    val msgHelp = messageFactory.createMessage(
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
    """.trimIndent()
    )

    val msgNotImplemented = messageFactory.createMessage("<red>Not implemented yet :')</red>")

    private val moduleManager = module.plugin.moduleManager

    override fun execute(context: BasicsCommandContext): CommandResult {

        val args = context.args
        val sender = context.sender
        if (args.isEmpty()) {
            showMainHelp(sender)
            return CommandResult.SUCCESS
        }

        val subCommand = args[0]

        when (subCommand) {
            "list" -> return listModules(sender)
        }

        args.removeAt(0)
        if (args.isEmpty()) {
            //messageFactory.createMessage("<red>Usage: /module $subCommand <module></red>").sendToSender(sender)
            //return CommandResult.SUCCESS
            return CommandResult.usage("$subCommand <module>")
        }

        when (subCommand) {
            "info" -> return showInfo(sender, requireModule(sender, args[0]))
            "enable" -> return enableModule(sender, requireModule(sender, args[0]))
            "disable" -> return disableModule(sender, requireModule(sender, args[0]))
            "reloadjar" -> return reloadJar(sender, requireModule(sender, args[0]))
            "reload" -> return reloadModule(sender, requireModule(sender, args[0]))
            "unload" -> return unloadModule(sender, requireModule(sender, args[0]))
            "load" -> return loadModule(sender, context)
            else -> {
                failInvalidArgument(subCommand)
            }
        }

        msgNotImplemented.sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun showInfo(sender: CommandSender, module: BasicsModule): CommandResult {
        val provider = ModuleTagProvider(module)
        messageFactory.createMessage(listOf(
            "<gold>Module Info:</gold>",
            "<gold>Name:</gold> <gray><#module></gray>",
            "<gold>Version:</gold> <gray><#version></gray>",
            "<gold>Description:</gold> <gray><#module_description></gray>",
        )).tags(provider).sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun loadModule(sender: CommandSender, context: BasicsCommandContext): CommandResult {
        context.readFlags()
        val enable = context.popFlag("--enable") or context.popFlag("-e")
        val arg = context.args[0]
        val file = moduleManager.modulesDirectory.resolve(arg)
        if (!file.exists()) {
            messageFactory.createMessage("<red>File $arg not found.</red>").sendToSender(sender)
            return CommandResult.SUCCESS
        }
        val result = moduleManager.loadModuleFromFile(file)
        val message = if (result.isSuccess)
            "<gold>Module ${result.getOrThrow().info.name} <green><bold>LOADED</bold></green>.</gold>"
         else
            "<red>Failed to load module $arg.</red>"

        messageFactory.createMessage(message).sendToSender(sender)

        if (result.isSuccess && enable) {
            enableModule(sender, result.getOrThrow())
        }

        return CommandResult.SUCCESS
    }

    private fun unloadModule(sender: CommandSender, module: BasicsModule): CommandResult {
        if(module.isEnabled()) {
            moduleManager.disableModule(module).get() // TODO: This is blocking, but it shouldn't be
            messageFactory.createMessage("<gold>Module ${module.info.name} <red>disabled</red>.</gold>")
                .sendToSender(sender)
        }
        moduleManager.unloadModule(module, true)
        messageFactory.createMessage("<gold>Module ${module.info.name} <red><bold>UNLOADED</bold></red>.</gold>").sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun requireModule(sender: CommandSender, arg: String): BasicsModule {
        val module = moduleManager.getModule(arg)
        if (module == null) {
            messageFactory.createMessage("<red>Module $arg not found").sendToSender(sender)
            throw BasicsCommandException(CommandResult.SUCCESS)
        }
        return module
    }

    private fun reloadModule(sender: CommandSender, module: BasicsModule): CommandResult {
        if (!module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is not enabled.</red>").sendToSender(sender)
            return CommandResult.SUCCESS
        }
        module.reloadConfig()
        messageFactory.createMessage("<gold>Module ${module.info.name} <green>reloaded</green>.</gold>")
            .sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun reloadJar(sender: CommandSender, module: BasicsModule): CommandResult {
        val enable = module.isEnabled()
        val file = module.file
        unloadModule(sender, module)

        val result = moduleManager.loadModuleFromFile(file)
        val message = if (result.isSuccess)
            "<gold>Module ${result.getOrThrow().info.name} <green><bold>LOADED</bold></green>.</gold>"
        else
            "<red>Failed to load module $file.</red>"

        messageFactory.createMessage(message).sendToSender(sender)

        if (result.isSuccess && enable) {
            enableModule(sender, result.getOrThrow())
        }

        return CommandResult.SUCCESS

    }


    private fun enableModule(sender: CommandSender, module: BasicsModule): CommandResult {
        if (module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is already enabled.</red>")
                .sendToSender(sender)
            return CommandResult.SUCCESS
        }
        moduleManager.enableModule(module, true)
        messageFactory.createMessage("<gold>Module ${module.info.name} <green>enabled</green>.</gold>")
            .sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun disableModule(sender: CommandSender, module: BasicsModule): CommandResult {
        if (!module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is already disabled.</red>")
                .sendToSender(sender)
            return CommandResult.SUCCESS
        }
        moduleManager.disableModule(module).get() // TODO: This is blocking, but it shouldn't be
        messageFactory.createMessage("<gold>Module ${module.info.name} <red>disabled</red>.</gold>")
            .sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun listModules(sender: CommandSender): CommandResult {
        val list = mutableListOf<Message>()
        moduleManager.loadedModules.sortedBy { it.info.name }.forEach { module ->
            val color = if (module.isEnabled()) "<green>" else "<red>"
            val colorEnd = if (module.isEnabled()) "</green>" else "</red>"
            list.add(messageFactory.createMessage("$color${module.info.name}$colorEnd"))
        }
        messageFactory.createMessage("<gold>Loaded Modules:</gold>").sendToSender(sender)
        sender.sendMessage(list.joinToString { it.toLegacyString() })
        return CommandResult.SUCCESS
    }

    private fun showMainHelp(sender: CommandSender) {
        msgHelp.sendToSender(sender)
    }

    // Tab Completes
    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        val args = context.args
        if (args.size == 1) {
            return listOf("list", "info", "enable", "disable", "reload", "unload", "load", "reloadjar").partialMatches(args[0])
        }
        if (args.size == 2) {
            when (args[0]) {
                "disable", "reload" -> return enabledModules().partialMatches(args[1])
                "info", "unload", "reloadjar" -> return allModules().partialMatches(args[1])
                "load" -> return filesNamesUnloadedModules().partialMatches(args[1]).addAnd("-e").addAnd("--enable")
                "enable" ->  return disabledModules().partialMatches(args[1])
            }
        }
        if(args.size == 3) {
            if(args[0] == "load") {
                if(args[1] == "-e" || args[1] == "--enable") {
                    return filesNamesUnloadedModules().partialMatches(args[2])
                }
            }
        }

        return mutableListOf()
    }

    private fun filesNamesUnloadedModules(): List<String> {
        return moduleManager.modulesDirectory.listFiles()?.filter {
            for (module in moduleManager.loadedModules) {
                if (module.file == it) {
                    return@filter false
                }
            }
            return@filter true
        }?.map { it.name } ?: listOf()
    }

    private fun enabledModules(): List<String> {
        return moduleManager.enabledModules.map { it.info.name }
    }

    private fun disabledModules(): List<String> {
        return moduleManager.disabledModules.map { it.info.name }
    }

    private fun allModules(): List<String> {
        return moduleManager.loadedModules.map { it.info.name }
    }

}
