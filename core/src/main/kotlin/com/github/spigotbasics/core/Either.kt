package com.github.spigotbasics.core


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
    fun <T> fold(fnL: (L) -> T, fnR: (R) -> T): T =
        when (this) {
            is Left -> fnL(value)
            is Right -> fnR(value)
        }
}

/**
 * Returns the left value or null if this is a [Either.Right].
 *
 * @return The left value, or null if this is a [Either.Right].
 */
fun <L, R> Either<L, R>.leftOrNull(): L? = (this as? Either.Left<L>)?.value

/**
 * Returns the right value or null if this is a [Either.Left].
 *
 * @return The right value, or null if this is a [Either.Left].
 */
fun <L, R> Either<L, R>.rightOrNull(): R? = (this as? Either.Right<R>)?.value
