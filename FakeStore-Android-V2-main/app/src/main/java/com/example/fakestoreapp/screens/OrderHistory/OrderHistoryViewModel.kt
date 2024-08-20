package com.example.fakestoreapp.screens.OrderHistory


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.data.Order
import com.example.fakestoreapp.repositories.OrderRepository
import com.example.fakestoreapp.screens.ShoppingCart.CartItem
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class OrderHistoryViewModel : ViewModel() {
    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory

    init {
        fetchOrders()
    }


    fun fetchOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = OrderRepository.getAllOrders()
            _orderHistory.value = orders
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addToOrderHistory(shoppingCart: ShoppingCart) {
        viewModelScope.launch(Dispatchers.IO) {

            val newOrder = Order(0, shoppingCart, LocalDateTime.now())
            OrderRepository.insertOrder(newOrder)


            val orders = OrderRepository.getAllOrders()
            _orderHistory.value = orders

        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            OrderRepository.deleteOrder(order)
            val orders = OrderRepository.getAllOrders()
            _orderHistory.value = orders
        }
    }

}
