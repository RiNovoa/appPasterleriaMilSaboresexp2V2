package com.example.proyectologin005d.data.database

import androidx.room.TypeConverter
import com.example.proyectologin005d.data.model.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCartItemList(value: List<CartItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCartItemList(value: String): List<CartItem> {
        val gson = Gson()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(value, type)
    }
}
