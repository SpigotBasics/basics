package com.github.spigotbasics.core

sealed class EitherResult<out A, out B> {
    class Success<A>(val value: A): EitherResult<A, Nothing>()
    class Failed<B>(val value: B): EitherResult<Nothing, B>()
}