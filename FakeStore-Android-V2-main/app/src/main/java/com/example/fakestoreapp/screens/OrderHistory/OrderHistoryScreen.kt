package com.example.fakestoreapp.screens.OrderHistory

import OrderDetailsViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fakestoreapp.design.AppTheme
import com.example.fakestoreapp.screens.Shared.BackButton
import com.example.fakestoreapp.screens.Shared.OrderHistoryButton
import com.example.fakestoreapp.screens.Shared.ShoppingCartButton
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderHistoryScreen(viewModel: OrderHistoryViewModel, orderDetailsViewModel: OrderDetailsViewModel, navController: NavController) {
    val orderHistoryState = viewModel.orderHistory.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchOrders()
    }
    AppTheme {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(navController)

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = "Order History",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))

                ShoppingCartButton(navController)
                OrderHistoryButton(navController)
            }

            LazyColumn(modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background)) {
                items(orderHistoryState.value) { order ->
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .padding(4.dp)
                            .clickable {
                                orderDetailsViewModel.setOrder(order)
                                navController.navigate("orderDetailsScreen")
                            },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                    ) {
                        Column{
                            Text(
                                text = "Order placed on: ${order.timestamp.format(dateFormatter)}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(4.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            order.cart.cartItems.forEachIndexed { index, cartItem ->
                                    Text(
                                    text = "${index + 1}. ${cartItem.product.title} - \$${cartItem.product.price} x ${cartItem.quantity}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    modifier = Modifier.padding(4.dp),
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            Text(
                                text = "Items: ${order.cart.cartItems.sumOf { it.quantity }}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.padding(4.dp),
                            )

                            val totalCost = order.cart.cartItems.sumOf { it.product.price * it.quantity } + 60
                            val formattedTotalCost = String.format("%.2f", totalCost)
                            Text(
                                text = "Total cost: \$$formattedTotalCost",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(4.dp),
                            )
                            // cancel order button
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.deleteOrder(order)
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "Cancel Order",
                                        color = Color.White,
                                        modifier = Modifier.padding(4.dp),
                                        fontSize = 16.sp,
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

    }

}
