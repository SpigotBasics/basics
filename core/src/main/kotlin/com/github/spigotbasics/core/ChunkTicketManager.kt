package com.github.spigotbasics.core

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import com.github.spigotbasics.core.model.SimpleChunkLocation
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap

class ChunkTicketManager {
    private val perChunkTickets = ConcurrentHashMap<SimpleChunkLocation, MutableSet<BasicsModule>>()
    private val perModuleTickets = ConcurrentHashMap<BasicsModule, MutableSet<SimpleChunkLocation>>()

    @Throws(WorldNotLoadedException::class)
    fun addTicket(
        module: BasicsModule,
        chunk: SimpleChunkLocation,
    ) {
        if (perChunkTickets.computeIfAbsent(chunk) { mutableSetOf() }.add(module)) {
            chunk.getWorld().addPluginChunkTicket(chunk.x, chunk.z, module.plugin as Plugin)
        }
        perModuleTickets.computeIfAbsent(module) { mutableSetOf() }.add(chunk)
    }

    @Throws(WorldNotLoadedException::class)
    fun removeTicket(
        module: BasicsModule,
        chunk: SimpleChunkLocation,
    ) {
        val modules = perChunkTickets[chunk]
        if (modules != null) {
            modules.remove(module)
            if (modules.isEmpty()) {
                chunk.getWorld().removePluginChunkTicket(chunk.x, chunk.z, module.plugin as Plugin)
                perChunkTickets.remove(chunk)
            }
        }
        perModuleTickets[module]?.remove(chunk)
        if (perModuleTickets[module]?.isEmpty() == true) {
            perModuleTickets.remove(module)
        }
    }

    fun removeAllTickets(module: BasicsModule) {
        val chunks = mutableListOf<SimpleChunkLocation>()
        perModuleTickets[module]?.forEach { chunkLocation ->
            chunks += chunkLocation
        }
        chunks.forEach { chunk ->
            removeTicket(module, chunk)
        }
    }
}
