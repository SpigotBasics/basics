package com.github.spigotbasics.core.storage

data class MySQLDatabaseInfo(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val tablePrefix: String,
)
