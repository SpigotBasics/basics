package com.github.spigotbasics.core

sealed class Either<out L, out R> {

    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    val isLeft get() = this is Left<L>
    val isRight get() = this is Right<R>

    fun <T> fold(fnL: (L) -> T, fnR: (R) -> T): T =
        when (this) {
            is Left -> fnL(value)
            is Right -> fnR(value)
        }
}

fun <L, R> Either<L, R>.leftOrNull(): L? = (this as? Either.Left<L>)?.value
fun <L, R> Either<L, R>.rightOrNull(): R? = (this as? Either.Right<R>)?.value