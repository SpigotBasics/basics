package com.github.spigotbasics.core.module.loader

import java.io.File
import java.net.URL
import java.net.URLClassLoader

class ModuleJarClassLoader(val file: File, val parentLoader: ClassLoader) : URLClassLoader(arrayOf( file.toURI().toURL()), parentLoader) {

}
