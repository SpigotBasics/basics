package com.github.spigotbasics.common.data

import java.sql.Connection
import kotlin.jvm.Throws

interface DataProvider {

    @Throws(Exception::class)
    fun connect(connectionString: String)

    @Throws(Exception::class)
    fun disconnect()

    fun connection(): Connection
}
