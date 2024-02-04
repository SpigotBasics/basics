package com.github.spigotbasics.modules.basicshomes.data

data class HomeList(private val homes: MutableMap<String, Home> = mutableMapOf()) {

    fun addHome(home: Home) {
        homes[home.name.lowercase()] = home
    }

    val size: Int
        get() = homes.size

    fun isEmpty(): Boolean {
        return homes.isEmpty()
    }

    fun removeHome(home: Home) {
        homes.remove(home.name.lowercase())
    }

    fun getHome(name: String): Home? {
        return homes[name.lowercase()]
    }

    fun homeExists(name: String): Boolean {
        return homes.containsKey(name.lowercase())
    }

    fun listHomeNames(): List<String> = homes.keys.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it })

    fun listHomes(): List<Home> = homes.values.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

}
