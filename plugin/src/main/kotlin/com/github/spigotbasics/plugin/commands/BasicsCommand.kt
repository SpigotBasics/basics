package com.github.spigotbasics.plugin.commands

import com.github.spigotbasics.libraries.co.aikar.commands.BaseCommand
import com.github.spigotbasics.libraries.co.aikar.commands.CommandHelp
import com.github.spigotbasics.libraries.co.aikar.commands.annotation.*
import com.github.spigotbasics.core.module.ModuleAlreadyLoadedException
import com.github.spigotbasics.plugin.BasicsPluginImpl
import org.bukkit.command.CommandSender


@CommandAlias("basics")
@Description("The base basics command")
class BasicsCommand(val plugin: BasicsPluginImpl) : BaseCommand() {

    @HelpCommand
    @Default
    fun doHelp(help: CommandHelp) {
        help.showHelp()
    }

    @Subcommand("reloadConfig")
    @CommandPermission("basics.reloadconfig")
    @CommandCompletion("@${CommandCompletionIDs.ENABLED_MODULES_AND_CORE}")
    fun reloadConfig(sender: CommandSender, moduleName: String) {

        if(moduleName == "core") {
            plugin.reloadCoreConfig()
            sender.sendMessage("§aBasics Core config reloaded")
            return
        }

        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        module.reloadConfig()
        sender.sendMessage("§aConfig of $moduleName reloaded")
    }

    @Subcommand("module loadAll")
    @CommandPermission("basics.module.load")
    fun loadAllModules(sender: CommandSender) {
        plugin.moduleManager.loadAllModulesFromModulesFolder()
        sender.sendMessage("Loaded all available modules.")
    }

    @Subcommand("module enable")
    @CommandCompletion("@${CommandCompletionIDs.DISABLED_MODULES}")
    @CommandPermission("basics.module.enable")
    fun enableModule(sender: CommandSender, moduleName: String) {
        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        if (module.isEnabled()) {
            sender.sendMessage("§cModule $moduleName is already enabled")
            return
        }
        plugin.moduleManager.enableModule(module, true)
        sender.sendMessage("§aModule $moduleName has been enabled.")
    }

    @Subcommand("module disable")
    @CommandCompletion("@${CommandCompletionIDs.ENABLED_MODULES}")
    @CommandPermission("basics.module.disable")
    fun disableModule(sender: CommandSender, moduleName: String) {
        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        if (!module.isEnabled()) {
            sender.sendMessage("§cModule $moduleName is already disabled")
            return
        }
        plugin.moduleManager.disableModule(module)
        sender.sendMessage("§aModule $moduleName has been disabled.")
    }

    @Subcommand("module list enabled")
    @CommandPermission("basics.module.list")
    fun listEnabledModules(sender: CommandSender) {
        val enabledModulesList = plugin.moduleManager.enabledModules.joinToString(", ") { it.info.name }
        sender.sendMessage("Enabled Modules:")
        sender.sendMessage("- " + enabledModulesList)
    }

    @Subcommand("module list disabled")
    @CommandPermission("basics.module.list")
    fun listDisabledModules(sender: CommandSender) {
        val disabledModulesList = plugin.moduleManager.disabledModules.joinToString(", ") { it.info.name }
        sender.sendMessage("Disabled Modules:")
        sender.sendMessage("- " + disabledModulesList)
    }

    @Subcommand("module list")
    @CommandPermission("basics.module.list")
    fun listAllModules(sender: CommandSender) {
        listEnabledModules(sender)
        listDisabledModules(sender)
    }

    @Subcommand("module info")
    @CommandCompletion("@${CommandCompletionIDs.LOADED_MODULES}")
    @CommandPermission("basics.module.info")
    fun moduleInfo(sender: CommandSender, moduleName: String) {
        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        sender.sendMessage("Module Info:")
        sender.sendMessage("- Name: ${module.info.name}")
        sender.sendMessage("- Version: ${module.info.version}")
        sender.sendMessage("- Enabled: ${module.isEnabled()}")
    }

    @Subcommand("module unload")
    @CommandCompletion("@${CommandCompletionIDs.LOADED_MODULES}")
    @CommandPermission("basics.module.unload")
    fun unloadModule(sender: CommandSender, moduleName: String) {
        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        if (module.isEnabled()) {
            sender.sendMessage("§cModule $moduleName is enabled, disable it now...")
            plugin.moduleManager.disableModule(module)
        }
        plugin.moduleManager.unloadModule(module)
        sender.sendMessage("§aModule $moduleName unloaded")
    }

    @Subcommand("module load")
    @CommandCompletion("@${CommandCompletionIDs.ALL_MODULE_FILES}")
    @CommandPermission("basics.module.load")
    fun loadModule(sender: CommandSender, moduleFile: String) {
        val file = plugin.moduleFolder.resolve(moduleFile)
        if (!file.exists()) {
            sender.sendMessage("§cModule file ${file.name} not found")
            return
        }
        if(file.parentFile != plugin.moduleFolder) {
            sender.sendMessage("§cModule file ${file.name} is not in the modules folder")
            return
        }
        val result = try {
            plugin.moduleManager.loadModuleFromFile(file)
        } catch (e: ModuleAlreadyLoadedException) {
            sender.sendMessage("§cModule from file ${file.name} is already loaded")
            return
        } catch (e: Exception) {
            sender.sendMessage("§cFailed to load module from file ${file.name}, see console.")
            e.printStackTrace()
            return
        }
        val module = result.getOrThrow()
        sender.sendMessage("§aModule ${module.info.nameAndVersion} loaded (but not enabled)")
    }
}
