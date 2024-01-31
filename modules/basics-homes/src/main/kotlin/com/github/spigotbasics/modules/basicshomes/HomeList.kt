package com.github.spigotbasics.modules.basicshomes

data class HomeList(val homes: MutableMap<String,Home> = mutableMapOf()) {

    fun addHome(name: String, home: Home) {
        homes[name] = home
    }

    fun removeHome(name: String) {
        homes.remove(name)
    }

    fun getHome(name: String): Home? {
        return homes[name]
    }

}
