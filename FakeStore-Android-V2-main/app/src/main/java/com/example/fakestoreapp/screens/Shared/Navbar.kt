package com.example.fakestoreapp.screens.Shared

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme


@Composable
fun Navbar(navController: NavController) {



}
@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("productListScreen") }) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Products List Screen", tint = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun ShoppingCartButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("shoppingCartScreen") }) {
        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Shopping Cart", tint = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun OrderHistoryButton(navController: NavController) {
    IconButton(onClick = { navController.navigate("orderHistoryScreen") }) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Order History", tint = MaterialTheme.colorScheme.onSurface)
    }
}