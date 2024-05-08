package com.github.spigotbasics.nms

import com.github.spigotbasics.common.MinecraftVersion
import com.github.spigotbasics.nms.v1_20_4.NMSImpl_v1_20_4
import com.github.spigotbasics.nms.v1_20_6.NMSImpl_v1_20_6

object NMSAggregator {
    fun getNmsFacade(version: MinecraftVersion): NMSFacade {
        try {
            // Latest
            if (version in MinecraftVersion.v1_20_3..MinecraftVersion.v1_20_4) {
                return NMSImpl_v1_20_4()
            }
            if (version in MinecraftVersion.v1_20_5..MinecraftVersion.v1_20_6) {
                return NMSImpl_v1_20_6()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        return UnsupportedNMSFacade()
    }
}
