package com.example.fakestoreapp.data.room

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.data.ProductRating
import com.example.fakestoreapp.screens.ShoppingCart.CartItem
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return Gson().toJson(list ?: listOf<String>())
    }

    @TypeConverter
    fun fromShoppingCart(shoppingCart: ShoppingCart): String {
        return Gson().toJson(shoppingCart)
    }

    @TypeConverter
    fun toShoppingCart(jsonString: String): ShoppingCart {
        return Gson().fromJson(jsonString, ShoppingCart::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(localDateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}



