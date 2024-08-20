package com.example.fakestoreapp.data.room

import androidx.room.*
import com.example.fakestoreapp.data.Order

@Dao
interface OrderDao {
    @Query("SELECT * FROM `order`")
    fun getAllOrders(): List<Order>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)


    @Delete
    suspend fun deleteOrder(order: Order)


}
