package com.github.spigotbasics.core.command

interface BasicsTabCompleter {
    fun tabComplete(context: BasicsCommandContext): MutableList<String>?
}
