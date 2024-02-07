package com.github.spigotbasics.pipe

import com.github.spigotbasics.pipe.exceptions.UnsupportedServerSoftwareException
import org.bukkit.entity.Player


interface OpenInventoryFacade {

    @Throws(UnsupportedServerSoftwareException::class)
    fun openCartographyTable(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openCartographyTable(Location, boolean)")
    }

    @Throws(UnsupportedServerSoftwareException::class)
    fun openLoom(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openLoom(Location, boolean)")
    }

    @Throws(UnsupportedServerSoftwareException::class)
    fun openGrindstone(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openGrindstone(Location, boolean)")
    }

    @Throws(UnsupportedServerSoftwareException::class)
    fun openSmithingTable(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openSmithingTable(Location, boolean)")
    }

    @Throws(UnsupportedServerSoftwareException::class)
    fun openStonecutter(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openStonecutter(Location, boolean)")
    }

    @Throws(UnsupportedServerSoftwareException::class)
    fun openAnvil(player: Player) {
        throw UnsupportedServerSoftwareException("HumanEntity#openAnvil(Location, boolean)")
    }

}