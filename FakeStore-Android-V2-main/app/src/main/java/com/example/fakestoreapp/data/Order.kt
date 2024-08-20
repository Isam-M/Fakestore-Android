package com.example.fakestoreapp.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fakestoreapp.screens.ShoppingCart.CartItem
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCart
import java.time.LocalDateTime
@Entity
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cart: ShoppingCart,
    val timestamp: LocalDateTime
)
