package com.github.spigotbasics.core

import java.io.InputStream
import java.net.URL

object SafeResourceGetter {

    private fun addSlash(path: String): String = if (path.startsWith("/")) path else "/$path"

    fun getResourceAsStream(clazz: Class<*>, path: String): InputStream? {
        //println("Getting resource $path for class ${clazz.name} - ClassLoader: ${clazz.classLoader?.javaClass?.name}")
        return clazz.classLoader?.getResourceAsStream(addSlash(path))
            ?: clazz.getResourceAsStream(addSlash(path))
    }

    fun getResource(clazz: Class<*>, path: String): URL? {
        return clazz.classLoader?.getResource(addSlash(path))
            ?: clazz.getResource(addSlash(path))
    }
}