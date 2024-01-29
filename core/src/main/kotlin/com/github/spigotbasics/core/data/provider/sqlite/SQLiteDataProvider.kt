package com.github.spigotbasics.core.data.provider.sqlite

import com.github.spigotbasics.core.data.DataProvider
import org.jetbrains.exposed.sql.Database

class SQLiteDataProvider : DataProvider {

    private var database: Database? = null

    override fun connect(connectionString: String) {
        this.database = Database.connect(connectionString)
    }

    override fun disconnect() {
        this.database = null
    }

    override fun connection(): Database {
        return this.database!!
    }

}
