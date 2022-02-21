package com.example.myplayer.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringTypeConverter {
    @TypeConverter
    fun jsonToStringArray(json: String?): List<String> {
        if (json == null) {
            return mutableListOf()
        }
        val type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun stringArrayToJson(array: List<String?>?): String {
        return gson.toJson(array)
    }

    companion object {
        private val gson = Gson()
    }
}
