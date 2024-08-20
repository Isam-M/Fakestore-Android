package com.example.fakestoreapp.repositories

import android.content.Context
import androidx.room.Room
import com.example.fakestoreapp.data.Cart
import com.example.fakestoreapp.data.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CartRepository {

    private lateinit var _appDatabase: AppDatabase
    private val _cartDao by lazy { _appDatabase.cartDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).fallbackToDestructiveMigration().build()
    }

    suspend fun getCart(id: Int): Cart? {
        try {
            val localCart = withContext(Dispatchers.IO) {
                _cartDao.getCartById(id)
            }

            return localCart
        } catch (e: Exception) {
            println("Error retrieving cart: ${e.message}")
            return null
        }
    }

    suspend fun insertCart(cart: Cart) {
        try {
            withContext(Dispatchers.IO) {
                _cartDao.insertCart(cart)
            }
        } catch (e: Exception) {
            println("Error inserting cart: ${e.message}")
        }
    }

    suspend fun deleteCart(cart: Cart) {
        try {
            withContext(Dispatchers.IO) {
                _cartDao.deleteCart(cart)
            }
        } catch (e: Exception) {
            println("Error deleting cart: ${e.message}")
        }
    }
    suspend fun updateCart(cart: Cart) {
        try {
            withContext(Dispatchers.IO) {
                _cartDao.updateCart(cart)
            }
        } catch (e: Exception) {
            println("Error updating cart: ${e.message}")
        }
    }

}