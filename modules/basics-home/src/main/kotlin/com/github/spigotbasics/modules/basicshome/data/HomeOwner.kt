package com.github.spigotbasics.modules.basicshome.data

import java.util.UUID

class HomeOwner(val uuid: UUID, private val homes: MutableList<BasicHome>) {

    fun addHome(home: BasicHome): Boolean {
        return homes.add(home);
    }

    fun removeHome(home: BasicHome): Boolean {
        return homes.remove(home);
    }

    fun removeHome(homeName: String): Boolean {
        return homes.removeIf { it.name == homeName };
    }
}
