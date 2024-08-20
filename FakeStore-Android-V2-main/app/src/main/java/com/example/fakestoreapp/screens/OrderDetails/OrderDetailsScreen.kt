package com.example.fakestoreapp.screens.OrderDetails

import OrderDetailsViewModel
import androidx.compose.material.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fakestoreapp.screens.Shared.OrderHistoryButton
import com.example.fakestoreapp.screens.Shared.ShoppingCartButton
import com.example.fakestoreapp.screens.ShoppingCart.CartItem
import java.time.format.DateTimeFormatter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.fakestoreapp.design.AppTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import com.example.fakestoreapp.screens.ProductList.ProductItem


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderDetailsScreen(viewModel: OrderDetailsViewModel, navController: NavController, onBackButtonClick: () -> Unit = {}) {
    val order = viewModel.selectedOrder.collectAsState().value

    if (order != null) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val totalCost = order.cart.cartItems.sumOf { it.product.price * it.quantity } + 60

        AppTheme {

            AppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onBackButtonClick() },
                            modifier = Modifier.padding(horizontal = 8.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Your Order",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        ShoppingCartButton(navController)
                        OrderHistoryButton(navController)
                    }
                    Text(
                        text = "Order placed on: ${order.timestamp.format(dateFormatter)}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(order.cart.cartItems) { cartItem ->
                            ProductItem(cartItem)
                        }
                    }

                    Text(
                        text = "Total cost: \$$totalCost",
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}


@Composable
fun ProductItem(cartItem: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentColor = MaterialTheme.colorScheme.secondary,){
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.product.image,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cartItem.product.description,
                    color = MaterialTheme.colorScheme.onTertiary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Price: \$${cartItem.product.price} x ${cartItem.quantity}",
                )
            }
        }
    }
}