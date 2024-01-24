package com.github.spigotbasics.core.facade

import com.github.spigotbasics.core.Either
import java.lang.reflect.Method
import kotlin.reflect.KClass

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

    override fun apply(clazz: T, arg1: A1?, arg2: A2?): Either<R1, R2> {
        return try {
            if (method1 != null) {
                val result = invokeMethod(method1, clazz, arg1)
                @Suppress("UNCHECKED_CAST")
                Either.Left<R1>(result as? R1 ?: Unit as R1)
            } else {
                val result = invokeMethod(method2!!, clazz, arg2)
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
