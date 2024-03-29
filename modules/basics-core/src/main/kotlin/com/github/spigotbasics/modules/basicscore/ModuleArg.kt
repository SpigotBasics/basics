package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.command.CommandSender

open class ModuleArg(name: String, private val predicate: (BasicsModule) -> Boolean) : CommandArgument<BasicsModule>(name) {
    private val moduleManager by lazy { Basics.moduleManager } // TODO: DI

    override fun parse(
        sender: CommandSender,
        value: String,
    ): BasicsModule? {
        moduleManager.loadedModules.firstOrNull { it.info.name.equals(value, ignoreCase = true) }?.let {
            return if (predicate(it)) {
                it
            } else {
                null
            }
        }
        return null
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return moduleManager.loadedModules.filter(predicate).map { it.info.name }.partialMatches(typing)
    }

    class EnabledModules(name: String) : ModuleArg(name, BasicsModule::isEnabled)

    class DisabledModules(name: String) : ModuleArg(name, { !it.isEnabled() })

    class LoadedModules(name: String) : ModuleArg(name, { true })
}
