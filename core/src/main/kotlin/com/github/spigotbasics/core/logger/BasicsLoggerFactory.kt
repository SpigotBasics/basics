package com.github.spigotbasics.core.logger

import com.github.spigotbasics.core.module.BasicsModule
import java.io.File
import java.util.logging.Logger
import kotlin.reflect.KClass


object BasicsLoggerFactory {

    fun getLogger(name: String): Logger {
        return Logger.getLogger("Basics $name")
    }

    fun getModuleLogger(module: BasicsModule): Logger {
        return getLogger("Module/${module.info.name}")
    }

    fun getModuleLogger(module: BasicsModule, clazz: KClass<*>): Logger {
        return getLogger("Module/${module.info.name}/${clazz.simpleName}")
    }

    fun getConfigLogger(file: File): Logger {
        return getLogger("Config/${file.name}")
    }

    fun getCoreLogger(clazz: KClass<*>): Logger {
        if (clazz.java.classLoader != javaClass.classLoader) {
            throw IllegalArgumentException("Class must be loaded by the same classloader as this class")
        }
        return getLogger("Core/${clazz.simpleName}")
    }

}