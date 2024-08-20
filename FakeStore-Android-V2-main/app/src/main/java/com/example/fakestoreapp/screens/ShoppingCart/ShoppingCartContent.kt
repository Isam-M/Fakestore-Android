package com.example.fakestoreapp.screens.ShoppingCart

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.fakestoreapp.R
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.screens.OrderHistory.OrderHistoryViewModel
import com.example.fakestoreapp.utilities.LoadingAnimation


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartScreenContent(
    state: CartItemsState,
    shoppingCartViewModel: ShoppingCartViewModel,
    orderHistoryViewModel: OrderHistoryViewModel
){
    val refreshUI by shoppingCartViewModel.refreshUI

    LaunchedEffect(refreshUI) {
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(4.dp)
        ) {
            items(state.cartItems.cartItems) { cartItem ->
                CartItemView(
                    cartItem = cartItem,
                    onQuantityChange = { product, newQuantity ->
                        shoppingCartViewModel.updateCartItem(product, newQuantity)
                    },
                    onRemoveItem = { product ->
                        shoppingCartViewModel.removeCartItem(product)
                    }
                )
            }
            item {
                if (state.cartItems.cartItems.isNotEmpty()) {
                    CheckoutComponent(
                        state = state,
                        shoppingCartViewModel = shoppingCartViewModel,
                        orderHistoryViewModel = orderHistoryViewModel,

                        )
                }
            }
        }

        if (state.isLoading) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingAnimation(
                    circleSize = 16.dp,
                    circleColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        if (state.error != null) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (state.cartItems.cartItems.isEmpty()){
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your cart is empty", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Empty Cart",
                    tint = Color.Gray,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CheckoutComponent(
    state: CartItemsState,
    shoppingCartViewModel: ShoppingCartViewModel,
    orderHistoryViewModel: OrderHistoryViewModel,

    ) {
    val totalPrice = state.cartItems.cartItems.sumOf { it.product.price * it.quantity }
    val formattedTotalPrice = String.format("%.2f", totalPrice)
    val totalPriceWithShipping = totalPrice + 60
    val formattedTotalPriceWithShipping  = String.format("%.2f", totalPriceWithShipping)

    val totalUniqueItems = state.cartItems.cartItems
        .groupBy { it.product }
        .entries
        .sumOf { entry ->
            entry.value.maxByOrNull { it.quantity }?.quantity ?: 0
        }


    Column(Modifier.padding(12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$totalUniqueItems items",color= MaterialTheme.colorScheme.primary)
            Text(
                text = "$$formattedTotalPrice",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Shipping fee",color= MaterialTheme.colorScheme.primary)
            Text(
                text = "$60.00", color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total", color= MaterialTheme.colorScheme.primary)
            Text(
                text = "$${formattedTotalPriceWithShipping}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                shoppingCartViewModel.checkout(orderHistoryViewModel)

            },
            shape = RoundedCornerShape(8),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = Color.White
            )


        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), text = "Place Order", textAlign = TextAlign.Center, color = Color.White

            )
        }
    }
}




@Composable
fun CartItemView(
    cartItem: CartItem,
    onQuantityChange: (Product, Int) -> Unit,
    onRemoveItem: (Product) -> Unit
) {

    Card(
        modifier = Modifier
            .padding(5.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .height(130.dp)
                ) {

            IconButton(
                onClick ={ onRemoveItem(cartItem.product) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (12).dp, y = -12.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Remove from cart",
                    tint = Color.Red
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                Card(
                    modifier = Modifier.width(70.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = cartItem.product.image)
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                    placeholder(R.drawable.ic_launcher_foreground)
                                }).build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 20.dp)

                ) {
                    Text(
                        text = cartItem.product.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,


                        )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${cartItem.product.price}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (50).dp, y = 10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick =  { onQuantityChange(cartItem.product, cartItem.quantity - 1) },

                            modifier = Modifier.offset(x = (12).dp, y = 0.dp)
                        ) {
                            Icon(
                                Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Decrease Quantity"
                            )
                        }

                        Text(
                            text = "${cartItem.quantity} Pc",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        IconButton(
                            onClick = { onQuantityChange(cartItem.product, cartItem.quantity + 1) },
                            modifier = Modifier.offset(x = (-12).dp, y = 0.dp)
                        ) {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = "Increase Quantity"
                            )
                        }

                    }
                }
            }
        }
    }
}
