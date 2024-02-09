package com.github.spigotbasics.core

import java.io.InputStream
import java.net.URL

/**
 * Provides safe and easy access to a resource. This will first try to get the resource from the class loader of the
 * given class, and if that fails, it will try to get it from [Class.getResourceAsStream] and [Class.getResource].
 *
 * It also adds a slash to the path if it doesn't start with one.
 *
 */
object SafeResourceGetter {
    private fun addSlash(path: String): String = if (path.startsWith("/")) path else "/$path"

    /**
     * Get resource as stream from the class loader of the given class, or from the class itself.
     *
     * @param clazz Class to get the resource from
     * @param path Path to the resource. If it doesn't start with a slash, one will be added.
     * @return Resource as stream, or null if it doesn't exist
     */
    fun getResourceAsStream(
        clazz: Class<*>,
        path: String,
    ): InputStream? {
        // println("Getting resource $path for class ${clazz.name} - ClassLoader: ${clazz.classLoader?.javaClass?.name}")
        return clazz.classLoader?.getResourceAsStream(addSlash(path))
            ?: clazz.getResourceAsStream(addSlash(path))
    }

    /**
     * Get resource as URL from the class loader of the given class, or from the class itself.
     *
     * @param clazz Class to get the resource from
     * @param path Path to the resource. If it doesn't start with a slash, one will be added.
     * @return Resource as URL, or null if it doesn't exist
     */
    fun getResource(
        clazz: Class<*>,
        path: String,
    ): URL? {
        return clazz.classLoader?.getResource(addSlash(path))
            ?: clazz.getResource(addSlash(path))
    }
}
