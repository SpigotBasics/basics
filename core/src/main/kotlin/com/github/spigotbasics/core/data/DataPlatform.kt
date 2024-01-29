package com.github.spigotbasics.core.data

import com.github.spigotbasics.core.data.DataProvider
import com.github.spigotbasics.core.data.provider.sqlite.SQLiteDataProvider

enum class DataPlatform(private val function: () -> DataProvider) {
    SQLITE({ SQLiteDataProvider() });

    fun creator(): () -> DataProvider {
        return function;
    }
}
