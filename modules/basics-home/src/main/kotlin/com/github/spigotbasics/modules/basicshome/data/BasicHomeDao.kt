package com.github.spigotbasics.modules.basicshome.data

import com.github.spigotbasics.libraries.org.jetbrains.exposed.sql.Table

object BasicHomeDao : Table() {
    val uuid = uuid("uuid")
    val homeName = text("homeName")
    val x = integer("x")
    val y = integer("y")
    val z = integer("z")
}
