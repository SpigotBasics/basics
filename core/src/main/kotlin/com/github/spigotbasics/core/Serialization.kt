package com.github.spigotbasics.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

object Serialization {
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // ISO 8601
        .create()

    fun <T> toJson(obj: T): JsonElement = gson.toJsonTree(obj)

    fun <T> fromJson(json: JsonElement, clazz: Class<T>): T = gson.fromJson(json, clazz)


}