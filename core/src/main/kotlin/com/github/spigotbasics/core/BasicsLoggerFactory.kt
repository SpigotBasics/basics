package com.github.spigotbasics.core

import com.github.spigotbasics.core.module.BasicsModule
import java.util.logging.Logger
import kotlin.reflect.KClass


object BasicsLoggerFactory {

    fun getLogger(name: String): Logger {
        return Logger.getLogger("Basics $name")
    }

    fun getModuleLogger(module: BasicsModule): Logger {
        return getLogger("Module/${module.info.name}")
    }

    fun getCoreLogger(clazz: KClass<*>): Logger {
        if (clazz.java.classLoader != javaClass.classLoader) {
            throw IllegalArgumentException("Class must be loaded by the same classloader as this class")
        }
        return getLogger("Core/${clazz.simpleName}")
    }

}