package com.github.spigotbasics.core.extensions

import java.io.File
import java.util.jar.JarFile
import kotlin.reflect.KClass

fun File.isJarFile(): Boolean {
    return this.extension == "jar"
}

fun File.isReallyJarFile(): Boolean {
    if(!isJarFile()) {
        return false
    }

    try {
        JarFile (this)
        return true
    } catch (e: Exception) {
        return false
    }
}

/**
 * Get all resources in the given directory as list of files
 *
 * @param directoryPath the directory path inside the .jar, e.g. "/META-INF"
 * @return the list of files
 */
fun KClass<*>.getAllResources(directoryPath: String): List<File> {
    val dir = File(this.java.getResource(directoryPath)?.file ?: throw IllegalArgumentException("directory does not exist"))
    return dir.walk()
        //.filter { it.isFile }
        .toList()
}