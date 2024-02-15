package com.github.spigotbasics.nms

import com.github.spigotbasics.common.MinecraftVersion
import com.github.spigotbasics.nms.v1_20_4.NMSImpl_v1_20_4

object NMSAggregator {
    fun getNmsFacade(version: MinecraftVersion): NMSFacade {
        try {
            // Latest
            if (version in MinecraftVersion.v1_20_3..MinecraftVersion.v1_20_4) {
                return NMSImpl_v1_20_4()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        return UnsupportedNMSFacade()
    }
}
