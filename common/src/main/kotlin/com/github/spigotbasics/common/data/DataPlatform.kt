package com.github.spigotbasics.common.data

import com.github.spigotbasics.common.data.provider.sqlite.SQLiteDataProvider

enum class DataPlatform(private val function: () -> DataProvider) {
    SQLITE({ SQLiteDataProvider() });

    fun creator(): () -> DataProvider {
        return function;
    }
}
