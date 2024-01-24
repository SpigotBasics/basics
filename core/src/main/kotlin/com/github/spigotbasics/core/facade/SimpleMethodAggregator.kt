package com.github.spigotbasics.core.facade

import com.github.spigotbasics.core.Either
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * A class for aggregating 2 methods from a specified class to be used for version-specific code, e.g.,
 * to support Components on Paper and Strings on Spigot.
 *
 * This class attempts to find and aggregate two methods from the specified class.
 * It can handle methods with 0 or 1 parameters and with or without return types.
 *
 * If the first method is found, it will be used. If it is not found, the second method will be used.
 * If neither method is found, an IllegalArgumentException will be thrown.
 *
 * @param T The type of the class from which methods are aggregated.
 * @param A1 The type of the first method's argument. Use `Unit` if the method has no parameters.
 * @param A2 The type of the second method's argument. Use `Unit` if the method has no parameters.
 * @param R1 The return type of the first method. Use `Unit` if the method has no return type.
 * @param R2 The return type of the second method. Use `Unit` if the method has no return type.
 * @param clazz The KClass of the target class.
 * @param methodName1 The name of the first method to aggregate.
 * @param argType1 The Class type of the first method's argument, or null if it has none.
 * @param methodName2 The name of the second method to aggregate.
 * @param argType2 The Class type of the second method's argument, or null if it has none.
 * @throws IllegalArgumentException if neither of the methods can be found in the class.
 */
open class SimpleMethodAggregator<T : Any, A1, A2, R1 : Any, R2 : Any>(
    private val clazz: KClass<T>,
    methodName1: String,
    private val argType1: Class<A1>?,
    methodName2: String,
    private val argType2: Class<A2>?
) : MethodAggregator<T, A1, A2, R1, R2> {

    private val method1: Method? = findMethod(methodName1, argType1)
    private val method2: Method? = method1 ?: findMethod(methodName2, argType2)

    init {
        if (method1 == null && method2 == null) {
            throw IllegalArgumentException("Couldn't find method $methodName1 or $methodName2 on class ${clazz.qualifiedName}")
        }
    }

    private fun findMethod(name: String, argType: Class<*>?): Method? {
        return try {
            if (argType == null) clazz.java.getMethod(name) else clazz.java.getMethod(name, argType)
        } catch (e: NoSuchMethodException) {
            null
        }
    }

    override fun apply(instance: T, arg1: A1?, arg2: A2?): Either<R1, R2> {
        return try {
            if (method1 != null) {
                val result = invokeMethod(method1, instance, arg1)
                @Suppress("UNCHECKED_CAST")
                Either.Left<R1>(result as? R1 ?: Unit as R1)
            } else {
                val result = invokeMethod(method2!!, instance, arg2)
                @Suppress("UNCHECKED_CAST")
                Either.Right<R2>(result as? R2 ?: Unit as R2)
            }
        } catch (e: Exception) {
            throw IllegalStateException("Error invoking method on ${this.clazz.qualifiedName}", e)
        }
    }

    private fun invokeMethod(method: Method, instance: T, arg: Any?): Any? {
        return if (method.parameterCount == 0) method.invoke(instance) else method.invoke(instance, arg)
    }
}
