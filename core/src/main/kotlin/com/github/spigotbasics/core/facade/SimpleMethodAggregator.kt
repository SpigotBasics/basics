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

    private val method1: Method?
    private val method2: Method?

    init {

        var method1: Method? = null
        var method2: Method? = null

        try {
            if(argType1 == null) {
                method1 = clazz.java.getMethod(methodName1)
            } else {
                method1 = clazz.java.getMethod(methodName1, argType1)
            }
        } catch (_: NoSuchMethodException) {

        }

        if(method1 == null) {
            try {
                if(argType2 == null) {
                    method2 = clazz.java.getMethod(methodName2)
                } else {
                    method2 = clazz.java.getMethod(methodName2, argType2)
                }
            } catch (_: NoSuchMethodException) {

            }
        }

        if(method1 == null && method2 == null) {
            throw IllegalArgumentException("Couldn't get method ${methodName1}(${argType1?.name ?: ""}) nor method $methodName2(${argType2?.name ?: ""}) on class ${clazz.qualifiedName}")
        }

        this.method1 = method1
        this.method2 = method2

    }

    override fun apply(clazz: T, arg1: A1?, arg2: A2?): Either<R1, R2> {
        try {
            if(method1 != null) {
                var result: R1?
                if(argType1 == null) {
                    result = method1.invoke(clazz) as? R1?
                } else {
                    result = method1.invoke(clazz, arg1) as? R1?
                }
                return Either.Left<R1>(result ?: Unit as R1)
            } else if(method2 != null) {
                var result: R2?
                if(argType2 == null) {
                    result = method2.invoke(clazz) as? R2?
                } else {
                    result = method2.invoke(clazz, arg2) as? R2?
                }
                return Either.Right<R2>(result ?: Unit as R2)
            } else {
                error("Both methods are null")
            }
        } catch (e: Exception) {
            val method = method1 ?: method2
            throw IllegalStateException("Could not call method ${method} on class ${this.clazz.qualifiedName}", e)
        }
    }
}