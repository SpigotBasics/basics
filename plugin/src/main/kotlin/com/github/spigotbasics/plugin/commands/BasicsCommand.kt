package com.github.spigotbasics.plugin.commands

import co.aikar.commands.ACFBukkitUtil.sendMsg
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import com.github.spigotbasics.plugin.BasicsPluginImpl
import org.bukkit.command.CommandSender


@CommandAlias("basics")
@Description("The base basics command")
class BasicsCommand(val plugin: BasicsPluginImpl) : BaseCommand() {

    @HelpCommand
    fun doHelp(sender: CommandSender, help: CommandHelp) {
        help.showHelp()
    }

    @Subcommand("module loadAll")
    fun loadAllModules(sender: CommandSender) {
        plugin.moduleManager.loadModules()
        val allModulesList = plugin.moduleManager.modules.joinToString(", ") { it.info.name }
        sender.sendMessage("Loaded all modules: $allModulesList")
    }

    @Subcommand("module enable")
    @CommandCompletion("@disabledmodules")
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
        plugin.moduleManager.enableModule(module)
        sender.sendMessage("§aModule $moduleName has been enabled.")
    }

    @Subcommand("module disable")
    @CommandCompletion("@enabledmodules")
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
    fun listEnabledModules(sender: CommandSender) {
        val enabledModulesList = plugin.moduleManager.enabledModules.joinToString(", ") { it.info.name }
        sender.sendMessage("Enabled Modules:")
        sender.sendMessage("- " + enabledModulesList)
    }

    @Subcommand("module list disabled")
    fun listDisabledModules(sender: CommandSender) {
        val disabledModulesList = plugin.moduleManager.disabledModules.joinToString(", ") { it.info.name }
        sender.sendMessage("Disabled Modules:")
        sender.sendMessage("- " + disabledModulesList)
    }

    @Subcommand("module list")
    fun listAllModules(sender: CommandSender) {
        listEnabledModules(sender)
        listDisabledModules(sender)
    }

    @Subcommand("module info")
    @CommandCompletion("@allmodules")
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
    @CommandCompletion("@disabledmodules")
    fun unloadModule(sender: CommandSender, moduleName: String) {
        val module = plugin.moduleManager.getModule(moduleName)
        if (module == null) {
            sender.sendMessage("§cModule $moduleName not found")
            return
        }
        if (module.isEnabled()) {
            sender.sendMessage("§cModule $moduleName is enabled, disable it first")
            return
        }
        plugin.moduleManager.unloadModule(module)
        sender.sendMessage("§aModule $moduleName unloaded")
    }
}
