package com.github.spigotbasics.core.command.parsed2

class Command<T : CommandContext>(
    private val executor: CommandExecutor<T>,
    private val paths: List<ArgumentPath<T>>,
) {
    fun execute(input: List<String>) {
        for (path in paths) {
            val context = path.parse(input)
            if (context != null) {
                executor.execute(context)
                return
            }
        }
        // Handle no matching path found, e.g., show usage or error message
    }
}
