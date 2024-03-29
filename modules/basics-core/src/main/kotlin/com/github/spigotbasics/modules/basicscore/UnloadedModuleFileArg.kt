package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.module.manager.ModuleManager
import org.bukkit.command.CommandSender

class UnloadedModuleFileArg(name: String, private val moduleManager: ModuleManager) : CommandArgument<String>(name) {
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

    override fun parse(
        sender: CommandSender,
        value: String,
    ): String? {
        return if (filesNamesUnloadedModules().contains(value)) {
            value
        } else {
            null
        }
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return filesNamesUnloadedModules().partialMatches(typing)
    }
}
