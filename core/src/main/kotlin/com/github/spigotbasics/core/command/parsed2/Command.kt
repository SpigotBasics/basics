package com.github.spigotbasics.core.command.parsed2

class Command(val paths: List<ArgumentPath>) {
    fun execute(input: String): CommandContext<*>? {
        val args = input.split(" ")
        for (path in paths) {
            val context = path.parse<Any>(args)
            if (context != null) return context
        }
        return null // No matching path found
    }
}