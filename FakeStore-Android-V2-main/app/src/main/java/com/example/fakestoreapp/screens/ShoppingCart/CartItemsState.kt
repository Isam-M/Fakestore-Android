package com.example.fakestoreapp.screens.ShoppingCart

import com.example.fakestoreapp.data.Product

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class ShoppingCart(
    val cartItems: MutableList<CartItem> = mutableListOf()
)


data class CartItemsState(
    val cartItems: ShoppingCart = ShoppingCart(),
    val isLoading: Boolean = false,
    val error: String? = null
)

