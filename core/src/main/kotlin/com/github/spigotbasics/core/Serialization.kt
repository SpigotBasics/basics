package com.github.spigotbasics.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object Serialization {
    private val gson =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // ISO 8601
            .create()

    fun <T> toJson(obj: T): JsonElement = gson.toJsonTree(obj)

    @Throws(JsonSyntaxException::class)
    fun <T> fromJson(
        json: JsonElement,
        clazz: Class<T>,
    ): T = gson.fromJson(json, clazz)

    fun <T> fromJson(
        json: JsonElement,
        token: TypeToken<T>,
    ): T = gson.fromJson(json, token.type)
}
