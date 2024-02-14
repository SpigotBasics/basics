package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.ParsedCommandContextExecutor
import org.bukkit.command.CommandSender

class TestContextExecutor : ParsedCommandContextExecutor<TestContext> {
    override fun execute(
        sender: CommandSender,
        context: TestContext,
    ) {
        sender.sendMessage("Test context: ${context.value}")
    }
}
