package com.github.spigotbasics.core.command.parsed

class GenericArgumentPathBuilder<T : ParsedCommandContext> : ArgumentPathBuilder<T>() {
    private var contextBuilder: ((Map<String, Any?>) -> T)? = null

    fun contextBuilder(contextBuilder: (Map<String, Any?>) -> T) = apply { this.contextBuilder = contextBuilder }

    override fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
            contextBuilder ?: error("Context builder not set"),
        )
}
