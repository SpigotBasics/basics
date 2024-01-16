package com.github.spigotbasics.core

sealed class Result<out A, out B> {
    class Success<A>(val value: A): Result<A, Nothing>()
    class Failed<B>(val value: B): Result<Nothing, B>()
}