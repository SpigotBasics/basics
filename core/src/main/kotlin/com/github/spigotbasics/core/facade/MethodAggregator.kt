package com.github.spigotbasics.core.facade

/**
 *
 *
 * @param C
 * @param A1
 * @param A2
 * @constructor Create empty Method beridge
 */
interface MethodAggregator<T : Any, A1, A2> {
    fun apply(event: T, arg1: A1?, arg2: A2?)
}