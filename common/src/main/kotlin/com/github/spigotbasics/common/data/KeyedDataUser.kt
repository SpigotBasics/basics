package com.github.spigotbasics.common.data

interface KeyedDataUser<K, E> {

    val provider: DataProvider

    fun fetch(key: K): E
    fun save(key: K, entry: E)

}
