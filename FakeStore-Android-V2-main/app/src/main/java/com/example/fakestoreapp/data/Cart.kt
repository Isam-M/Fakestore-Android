package com.example.fakestoreapp.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCart

@Entity(tableName = "cart")
data class Cart (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cart: ShoppingCart
)