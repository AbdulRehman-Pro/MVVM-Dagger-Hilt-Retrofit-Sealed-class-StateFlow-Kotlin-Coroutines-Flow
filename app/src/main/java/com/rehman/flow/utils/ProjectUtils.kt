package com.rehman.flow.utils

import com.google.gson.Gson
import com.google.gson.internal.Primitives
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object ProjectUtils {

    fun objectToString(value: Any?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    inline fun <reified T> String.stringToObject(): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(this, type)
    }
}