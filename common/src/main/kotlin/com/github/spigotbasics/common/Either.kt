package com.github.spigotbasics.common

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
