package com.github.spigotbasics.core.facade

import com.github.spigotbasics.core.Either

/**
 *
 *
 * @param C
 * @param A1
 * @param A2
 * @constructor Create empty Method beridge
 */
interface MethodAggregator<T : Any, A1, A2, R1 : Any, R2 : Any> {
    fun apply(clazz: T, arg1: A1?, arg2: A2?): Either<R1, R2>
}