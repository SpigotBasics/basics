package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.CommandArgument
import com.github.spigotbasics.core.module.BasicsModule

class ModuleArg(name: String, private val predicate: (BasicsModule) -> Boolean) : CommandArgument<BasicsModule>(name) {

    private val moduleManager by lazy { Basics.moduleManager } // TODO: DI

    override fun parse(value: String): BasicsModule? {
        moduleManager.loadedModules.firstOrNull { it.info.name.equals(value, ignoreCase = true) }?.let {
            return if(predicate(it))
                it
            else
                null
        }
    }
}