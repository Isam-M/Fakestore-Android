package com.example.fakestoreapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fakestoreapp.data.Cart

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAllCarts(): List<Cart>

    @Query("SELECT * FROM cart WHERE id = :cartId")
    fun getCartById(cartId: Int): Cart


    @Update
    suspend fun updateCart(cart: Cart)
    @Insert
    suspend fun insertCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)
}