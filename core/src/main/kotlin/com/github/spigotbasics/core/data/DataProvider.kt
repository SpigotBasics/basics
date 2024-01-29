package com.github.spigotbasics.core.data

import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import kotlin.jvm.Throws

interface DataProvider {

    @Throws(Exception::class)
    fun connect(connectionString: String)

    @Throws(Exception::class)
    fun disconnect()

    fun connection(): Database
}
