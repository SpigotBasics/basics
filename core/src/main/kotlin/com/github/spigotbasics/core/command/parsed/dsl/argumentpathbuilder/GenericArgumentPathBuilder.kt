package com.github.spigotbasics.core.command.parsed.dsl.argumentpathbuilder

import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.context.CommandContext

class GenericArgumentPathBuilder<T : CommandContext> : ArgumentPathBuilder<T>() {
    protected var contextBuilder: ((Map<String, Any?>) -> T)? = null

    fun contextBuilder(contextBuilder: (Map<String, Any?>) -> T) = apply { this.contextBuilder = contextBuilder }

    override fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
            contextBuilder ?: error("Context builder not set"),
        )
}
