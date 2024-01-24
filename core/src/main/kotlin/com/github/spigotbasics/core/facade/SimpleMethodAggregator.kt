package com.github.spigotbasics.core.facade

import java.lang.reflect.Method
import kotlin.reflect.KClass

open class SimpleMethodAggregator<T : Any, A1, A2>(
    private val clazz: KClass<T>,
    methodName1: String,
    argType1: Class<A1>,
    methodName2: String,
    argType2: Class<A2>
) : MethodAggregator<T, A1, A2> {

    private val method1: Method?
    private val method2: Method?

    init {

        var method1: Method? = null
        var method2: Method? = null

        try {
            method1 = clazz.java.getMethod(methodName1, argType1)
        } catch (_: NoSuchMethodException) {

        }

        if(method1 == null) {
            try {
                method2 = clazz.java.getMethod(methodName2, argType2)
            } catch (_: NoSuchMethodException) {

            }
        }

        if(method1 == null && method2 == null) {
            throw IllegalArgumentException("Couldn't get method {$methodName1}(${argType1.name}) nor method $methodName2(${argType2.name}) on class ${clazz.qualifiedName}")
        }

        this.method1 = method1
        this.method2 = method2

    }

    override fun apply(event: T, arg1: A1?, arg2: A2?) {
        try {
            if(method1 != null) {
                method1.invoke(event, arg1)
            } else if(method2 != null) {
                method2.invoke(event, arg2)
            } else {
                error("Both methods are null")
            }
        } catch (e: Exception) {
            val method = method1 ?: method2
            throw IllegalStateException("Could not call method ${method} on class ${clazz.qualifiedName}", e)
        }
    }
}