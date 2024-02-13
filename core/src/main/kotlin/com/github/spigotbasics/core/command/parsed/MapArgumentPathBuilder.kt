package com.github.spigotbasics.core.command.parsed

class MapArgumentPathBuilder : ArgumentPathBuilder<MapCommandContext>() {
    override fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
        ) { args -> MapCommandContext(args) }
}
