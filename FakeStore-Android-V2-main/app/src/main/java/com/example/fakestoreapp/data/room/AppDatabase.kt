package com.example.fakestoreapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fakestoreapp.data.Cart
import com.example.fakestoreapp.data.Order
import com.example.fakestoreapp.data.Product

@Database(
    entities = [Product::class, Cart::class, Order::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    abstract fun orderDao(): OrderDao
}



