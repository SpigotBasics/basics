package com.github.spigotbasics.common.data.provider.sqlite

import com.github.spigotbasics.common.data.DataProvider
import java.sql.Connection
import java.sql.DriverManager

class SQLiteDataProvider : DataProvider {

    private var connection: Connection? = null

    override fun connect(connectionString: String) {
        this.connection = DriverManager.getConnection(connectionString);
    }

    override fun disconnect() {
        this.connection?.close();
        this.connection = null
    }

    override fun connection(): Connection {
        return this.connection!!
    }

}
