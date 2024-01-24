package com.github.spigotbasics.core.facade

import com.github.spigotbasics.core.Either

/**
 * Interface for aggregating methods from a specified class to be used for version-specific code, e.g.,
 *  * to support Components on Paper and Strings on Spigot.
 *
 * This interface defines a contract for implementing classes to aggregate methods and apply them
 * dynamically based on the provided arguments and class instance.
 *
 * @param T The type of the class instance on which methods will be applied.
 * @param A1 The type of the first argument for the aggregated method. Use `Unit` if no argument is required.
 * @param A2 The type of the second argument for the aggregated method. Use `Unit` if no argument is required.
 * @param R1 The return type of the first method. Use `Unit` if the method has no return type.
 * @param R2 The return type of the second method. Use `Unit` if the method has no return type.
 * @property apply Invokes the aggregated method on the given class instance with the provided arguments.
 */
interface MethodAggregator<T : Any, A1, A2, R1 : Any, R2 : Any> {
    /**
     * @param instance The instance of the class on which the method is applied.
     * @param arg1 The first argument for the method. Can be null if the method does not require it.
     * @param arg2 The second argument for the method. Can be null if the method does not require it.
     * @return An [Either] instance representing the result of the method invocation, either [R1] or [R2].
     */
    fun apply(instance: T, arg1: A1?, arg2: A2?): Either<R1, R2>
}