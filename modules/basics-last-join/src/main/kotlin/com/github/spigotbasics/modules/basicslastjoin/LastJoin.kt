package com.github.spigotbasics.modules.basicslastjoin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.text.DateFormat
import java.time.LocalDateTime
import java.util.Date

data class LastJoin(val dateTime: Date = Date()) {
    companion object {
        val gson: Gson = GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create()

        fun fromJson(json: JsonObject): LastJoin {
            return gson.fromJson(json, LastJoin::class.java)
        }
    }

    fun toJson(): JsonObject {
        return gson.toJsonTree(this).asJsonObject
    }
}
