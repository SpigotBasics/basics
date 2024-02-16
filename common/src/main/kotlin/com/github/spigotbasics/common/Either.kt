package com.github.spigotbasics.common

import com.github.spigotbasics.common.Either.Left
import com.github.spigotbasics.common.Either.Right

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 *
 * @param L Type of [Left] value.
 * @param R Type of [Right] value.
 */
sealed class Either<out L, out R> {
    /**
     * Represents the left side
     *
     * @param L Type of the left value.
     * @property value The value of [Left].
     */
    data class Left<out L>(val value: L) : Either<L, Nothing>()

    /**
     * Represents the right side
     *
     * @param R Type of the right value.
     * @property value The value of [Right].
     */
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    /**
     * Checks if this either is a [Left].
     *
     * @return `true` if this is a [Left], `false` otherwise.
     */
    val isLeft get() = this is Left<L>

    /**
     * Checks if this either is a [Right].
     *
     * @return `true` if this is a [Right], `false` otherwise.
     */
    val isRight get() = this is Right<R>

    /**
     * Applies `fnL` if this is a [Left] or `fnR` if this is a [Right].
     *
     * @param fnL The function to apply if this is a [Left].
     * @param fnR The function to apply if this is a [Right].
     * @return The result of applying the function.
     */
    fun <T> fold(
        fnL: (L) -> T,
        fnR: (R) -> T,
    ): T =
        when (this) {
            is Left -> fnL(value)
            is Right -> fnR(value)
        }

    /**
     * Returns the left value or null if this is a [Either.Right].
     *
     * @return The left value, or null if this is a [Either.Right].
     */
    fun leftOrNull(): L? = (this as? Either.Left<L>)?.value

    /**
     * Returns the right value or null if this is a [Either.Left].
     *
     * @return The right value, or null if this is a [Either.Left].
     */
    fun rightOrNull(): R? = (this as? Either.Right<R>)?.value

    /**
     * Transforms the value inside this [Either] if it's a [Left] by applying [transform] to it.
     *
     * @param transform The transformation function to apply to the [Left] value.
     * @return An [Either] containing the transformed [Left] value, or the original [Right] value if this is a [Right].
     */
    fun <NewL> mapLeft(transform: (L) -> NewL): Either<NewL, R> =
        when (this) {
            is Left -> Left(transform(value))
            is Right -> this
        }

    /**
     * Transforms the value inside this [Either] if it's a [Right] by applying [transform] to it.
     *
     * @param transform The transformation function to apply to the [Right] value.
     * @return An [Either] containing the transformed [Right] value, or the original [Left] value if this is a [Left].
     */
    fun <NewR> mapRight(transform: (R) -> NewR): Either<L, NewR> =
        when (this) {
            is Left -> this
            is Right -> Right(transform(value))
        }

    /**
     * Transforms the values inside this [Either], applying [transformLeft] if it's a [Left]
     * or [transformRight] if it's a [Right].
     *
     * @param transformLeft The transformation function to apply to the [Left] value.
     * @param transformRight The transformation function to apply to the [Right] value.
     * @return An [Either] containing the transformed value.
     */
    fun <NewL, NewR> mapBoth(
        transformLeft: (L) -> NewL,
        transformRight: (R) -> NewR,
    ): Either<NewL, NewR> =
        when (this) {
            is Left -> Left(transformLeft(value))
            is Right -> Right(transformRight(value))
        }

    companion object {
        /**
         * Constructs an Either object from two nullable values. If both are null, or both are non-null, an exception is thrown.
         *
         * @param L? The nullable left value.
         * @param R? The nullable right value.
         * @return Either object encapsulating the non-null value.
         * @throws IllegalArgumentException if both L and R are null or non-null.
         */
        fun <L, R> of(
            l: L?,
            r: R?,
        ): Either<L, R> {
            return when {
                l != null && r != null -> throw IllegalArgumentException("Both L and R cannot be non-null")
                l != null -> Left(l)
                r != null -> Right(r)
                else -> throw IllegalArgumentException("Both L and R cannot be null")
            }
        }
    }
}
