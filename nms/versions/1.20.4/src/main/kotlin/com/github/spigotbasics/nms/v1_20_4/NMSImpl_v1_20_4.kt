package com.github.spigotbasics.nms.v1_20_4

import com.github.spigotbasics.nms.NMSFacade
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftServer

class NMSImpl_v1_20_4 : NMSFacade {
    override fun getTps(): DoubleArray {
        return (Bukkit.getServer() as CraftServer).handle.server.recentTps
    }
}
