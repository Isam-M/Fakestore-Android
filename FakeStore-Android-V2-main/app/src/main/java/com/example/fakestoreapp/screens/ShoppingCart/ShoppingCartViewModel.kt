package com.example.fakestoreapp.screens.ShoppingCart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.data.Cart
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.repositories.CartRepository
import com.example.fakestoreapp.screens.OrderHistory.OrderHistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingCartViewModel() : ViewModel() {

    private val _cartItemsState = MutableStateFlow(CartItemsState())
    val cartItemsState: StateFlow<CartItemsState> get() = _cartItemsState



    var shoppingCart = ShoppingCart()

    fun addToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedCart = shoppingCart.copy()

            val existingItem = updatedCart.cartItems.firstOrNull { it.product.id == product.id }

            if (updatedCart.cartItems.isEmpty()) {
                CartRepository.insertCart(Cart(1, updatedCart))
            } else {
                val existingCart = CartRepository.getCart(1)
                if (existingCart != null) {
                    updatedCart.cartItems.removeAll { item ->
                        existingCart.cart.cartItems.any { it.product.id == item.product.id }
                    }
                    updatedCart.cartItems.addAll(existingCart.cart.cartItems)
                }
            }
            if (existingItem != null) {
                val newQuantity = existingItem.quantity + 1
                updatedCart.cartItems.removeAll { it.product.id == product.id }
                updatedCart.cartItems.add(CartItem(product, newQuantity))
            } else {
                updatedCart.cartItems.add(CartItem(product, 1))
            }

            CartRepository.updateCart(Cart(1, updatedCart))
            Log.d("ShoppingCart", "Cart after adding product: $shoppingCart")
            updateCartState(updatedCart)
        }
    }


    private fun updateCartState(updatedShoppingCart: ShoppingCart) {
        _cartItemsState.value = CartItemsState(
            cartItems = updatedShoppingCart,
            isLoading = false,
            error = null
        )
        println("Cart state updated!")
    }
    var refreshUI = mutableStateOf(false)
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkout(orderHistoryViewModel: OrderHistoryViewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ShoppingCart", "Checkout with cart: $shoppingCart")


            val cartForOrderHistory = ShoppingCart(shoppingCart.cartItems.toMutableList())
            orderHistoryViewModel.addToOrderHistory(cartForOrderHistory)


            shoppingCart.cartItems.clear()
            CartRepository.deleteCart(Cart(1, ShoppingCart()))


            withContext(Dispatchers.Main) {
                updateCartState(ShoppingCart())
                refreshUI.value = !refreshUI.value
            }
        }
    }



    fun updateCartItem(product: Product, newQuantity: Int) {
        val newCartItems = shoppingCart.cartItems.map { cartItem ->
            if (cartItem.product.id == product.id) {
                cartItem.copy(quantity = newQuantity.coerceAtLeast(1))
            } else {
                cartItem
            }
        }
        shoppingCart = ShoppingCart(newCartItems.toMutableList())
        updateCartState(shoppingCart)
        viewModelScope.launch(Dispatchers.IO) {
            CartRepository.updateCart(Cart(1, shoppingCart))
        }
    }

    fun removeCartItem(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingCart.cartItems.removeAll { it.product.id == product.id }
            CartRepository.updateCart(Cart(1, shoppingCart))
            withContext(Dispatchers.Main) {
                updateCartState(shoppingCart)
                refreshUI.value = !refreshUI.value
            }
        }
    }


    fun initializeOrRefreshCart() {
        viewModelScope.launch(Dispatchers.IO) {
            val storedCart = CartRepository.getCart(1)
            val cart = storedCart?.cart ?: ShoppingCart()
            withContext(Dispatchers.Main) {
                shoppingCart = cart
                updateCartState(shoppingCart)
            }
        }
    }

}


