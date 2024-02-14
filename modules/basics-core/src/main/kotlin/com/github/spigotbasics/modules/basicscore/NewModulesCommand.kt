package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.command.CommandSender

class NewModulesCommand(private val module: BasicsCoreModule) : CommandContextExecutor<MapContext> {
    private val moduleManager: ModuleManager = module.plugin.moduleManager
    private val messageFactory: MessageFactory = module.messageFactory

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

    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val sub = (context["sub"] as String?) ?: "help"

        val module = context["module"] as BasicsModule?
        val moduleFileName = context["moduleFileName"] as String?

        when (sub) {
            "help" -> showMainHelp(sender)
            "list" -> listModules(sender)
            "info" -> showInfo(sender, module!!)
            "enable" -> enableModule(sender, module!!)
            "disable" -> disableModule(sender, module!!)
            "reloadjar" -> reloadJar(sender, module!!)
            "reload" -> reloadModule(sender, module!!)
            "unload" -> unloadModule(sender, module!!)
            "load" -> loadModule(sender, moduleFileName!!)
        }
    }

    private fun showInfo(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        val provider = ModuleTagProvider(module)
        messageFactory.createMessage(
            listOf(
                "<gold>Module Info:</gold>",
                "<gold>Name:</gold> <gray><#module></gray>",
                "<gold>Version:</gold> <gray><#version></gray>",
                "<gold>Description:</gold> <gray><#module_description></gray>",
            ),
        ).tags(provider).sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun loadModule(
        sender: CommandSender,
        moduleFileName: String,
    ): CommandResult {
//        context.readFlags()
//        val enable = context.popFlag("--enable") or context.popFlag("-e")
//        val arg = context.args[0]
        val enable = true // TODO: Implement flags
        val file = moduleManager.modulesDirectory.resolve(moduleFileName)
        if (!file.exists()) {
            messageFactory.createMessage("<red>File $moduleFileName not found.</red>").sendToSender(sender)
            return CommandResult.SUCCESS
        }
        val result = moduleManager.loadModuleFromFile(file)
        val message =
            if (result.isSuccess) {
                "<gold>Module ${result.getOrThrow().info.name} <green><bold>LOADED</bold></green>.</gold>"
            } else {
                "<red>Failed to load module $moduleFileName.</red>"
            }

        messageFactory.createMessage(message).sendToSender(sender)

        if (result.isSuccess && enable) {
            enableModule(sender, result.getOrThrow())
        }

        return CommandResult.SUCCESS
    }

    private fun unloadModule(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        if (module == this.module) {
            messageFactory.createMessage("<red>Cannot unload the core module.</red>").sendToSender(sender)
            return CommandResult.SUCCESS
        }
        if (module.isEnabled()) {
            moduleManager.disableModule(module).get() // TODO: This is blocking, but it shouldn't be
            messageFactory.createMessage("<gold>Module ${module.info.name} <red>disabled</red>.</gold>")
                .sendToSender(sender)
        }
        moduleManager.unloadModule(module, true)
        messageFactory.createMessage("<gold>Module ${module.info.name} <red><bold>UNLOADED</bold></red>.</gold>").sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun reloadModule(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        if (!module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is not enabled.</red>").sendToSender(sender)
            return CommandResult.SUCCESS
        }
        module.reloadConfig()
        messageFactory.createMessage("<gold>Module ${module.info.name} <green>reloaded</green>.</gold>")
            .sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun reloadJar(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        val enable = module.isEnabled()
        val file = module.file
        unloadModule(sender, module)

        val result = moduleManager.loadModuleFromFile(file)
        val message =
            if (result.isSuccess) {
                "<gold>Module ${result.getOrThrow().info.name} <green><bold>LOADED</bold></green>.</gold>"
            } else {
                "<red>Failed to load module $file.</red>"
            }

        messageFactory.createMessage(message).sendToSender(sender)

        if (result.isSuccess && enable) {
            enableModule(sender, result.getOrThrow())
        }

        return CommandResult.SUCCESS
    }

    private fun enableModule(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        if (module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is already enabled.</red>")
                .sendToSender(sender)
            return CommandResult.SUCCESS
        }
        moduleManager.enableModule(module, true, true)
        messageFactory.createMessage("<gold>Module ${module.info.name} <green>enabled</green>.</gold>")
            .sendToSender(sender)
        return CommandResult.SUCCESS
    }

    private fun disableModule(
        sender: CommandSender,
        module: BasicsModule,
    ): CommandResult {
        if (!module.isEnabled()) {
            messageFactory.createMessage("<red>Module ${module.info.name} is already disabled.</red>")
                .sendToSender(sender)
            return CommandResult.SUCCESS
        }
        if (module == this.module) {
            messageFactory.createMessage("<red>Cannot disable the core module.</red>").sendToSender(sender)
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
}
