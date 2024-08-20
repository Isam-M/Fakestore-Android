package com.example.fakestoreapp

import OrderDetailsViewModel
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakestoreapp.repositories.CartRepository
import com.example.fakestoreapp.repositories.OrderRepository
import com.example.fakestoreapp.repositories.ProductRepository
import com.example.fakestoreapp.screens.OrderDetails.OrderDetailsScreen

import com.example.fakestoreapp.screens.OrderHistory.OrderHistoryScreen
import com.example.fakestoreapp.screens.OrderHistory.OrderHistoryViewModel
import com.example.fakestoreapp.screens.ProductDetails.ProductDetailsScreen
import com.example.fakestoreapp.screens.ProductDetails.ProductDetailsViewModel
import com.example.fakestoreapp.screens.ProductList.ProductListScreen
import com.example.fakestoreapp.screens.ProductList.ProductListViewModel
import com.example.fakestoreapp.screens.ProductList.SearchProductComposable
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCartScreen
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCartViewModel
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme

class  MainActivity : ComponentActivity() {
    private val _productListViewModel: ProductListViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val _orderHistoryViewModel: OrderHistoryViewModel by viewModels()
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ProductRepository.initializeDatabase(this)
        CartRepository.initializeDatabase(this)
        OrderRepository.initializeDatabase(this)


        setContent {
            FakeStoreAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "productListScreen"
                ) {
                    composable("productListScreen") {
                        ProductListScreen(
                            viewModel = _productListViewModel,
                            onProductClick = { productId ->
                            navController.navigate("productDetailsScreen/$productId")
                        }, navController = navController)
                    }
                    composable("shoppingCartScreen") {
                        ShoppingCartScreen(
                            viewModel = _shoppingCartViewModel,
                            orderHistoryViewModel = _orderHistoryViewModel,
                            navController = navController
                        )
                    }

                    composable("orderHistoryScreen") {
                        OrderHistoryScreen(viewModel = _orderHistoryViewModel, orderDetailsViewModel = orderDetailsViewModel, navController = navController)
                    }

                    composable("orderDetailsScreen") {
                        OrderDetailsScreen(viewModel = orderDetailsViewModel,navController = navController, onBackButtonClick = { navController.popBackStack() } )
                    }



                    composable(
                        route = "searchProductScreen"
                    ) {
                        SearchProductComposable(_productListViewModel.searchProductViewModel, _productListViewModel)
                    }

                    composable(
                        route = "productDetailsScreen/{productId}",
                        arguments = listOf(
                            navArgument(name = "productId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId") ?: -1

                        LaunchedEffect(productId) {
                            _productDetailsViewModel.setSelectedProduct(productId)
                        }

                        ProductDetailsScreen(
                            viewModel = _productDetailsViewModel,
                            shoppingCartViewModel = _shoppingCartViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                            navController = navController
                        )
                    }

                }

            }
        }
    }
}


