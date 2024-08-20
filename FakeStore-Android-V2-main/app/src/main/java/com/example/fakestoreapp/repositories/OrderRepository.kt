package com.example.fakestoreapp.repositories

import android.content.Context
import androidx.room.Room
import com.example.fakestoreapp.data.Order
import com.example.fakestoreapp.data.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OrderRepository {
    private lateinit var _appDatabase: AppDatabase
    private val _orderDao by lazy { _appDatabase.orderDao() }
    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).fallbackToDestructiveMigration().build()
    }
    suspend fun insertOrder(order: Order) {
        withContext(Dispatchers.IO) {
            _orderDao.insertOrder(order)
        }
    }

    suspend fun getAllOrders(): List<Order> {
        return withContext(Dispatchers.IO) {
            _orderDao.getAllOrders()
        }
    }

    suspend fun deleteOrder(order: Order) {
        withContext(Dispatchers.IO) {
            _orderDao.deleteOrder(order)
        }
    }


}
