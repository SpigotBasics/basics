package com.github.spigotbasics.core.command.parsed.dsl.argumentpathbuilder

import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.context.MapContext

class MapArgumentPathBuilder : ArgumentPathBuilder<MapContext>() {
    override fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
        ) { args -> MapContext(args) }
}
