package com.example.fakestoreapp.screens.ProductDetails

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fakestoreapp.data.Review
import com.example.fakestoreapp.design.AppTheme
import com.example.fakestoreapp.screens.ShoppingCart.ShoppingCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    onBackButtonClick: () -> Unit = {},
    navController: NavController
) {

    val productState = viewModel.selectedProduct.collectAsState()
    val product = productState.value
    val selectedProduct = viewModel.selectedProduct.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (product == null) {
        Text(text = "Failed to get product details. Selected product is null")
        return
    }
    LaunchedEffect(product.id) {
        viewModel.getReviewsForProduct(product.id)
        println("Fetching reviews for product: ${product.id}")
    }

    // Popup go to cart or keep shopping
    if (showDialog.value) {
        AppTheme {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Added to Cart!") },
                text = { Text("Your item has been added to your cart.") },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { navController.navigate("shoppingCartScreen") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Go to Cart")
                        }

                        TextButton(
                            onClick = { navController.navigate("productListScreen") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Keep Shopping")
                        }
                    }
                }
            )

        }
    }
    AppTheme {

            if (!isLandscape) {
                ProductDetailsPortraitView(
                    viewModel = viewModel,
                    shoppingCartViewModel = shoppingCartViewModel,
                    onBackButtonClick = onBackButtonClick,
                    showDialog = showDialog
                )
            } else {
                ProductDetailsLandscapeView(
                    viewModel = viewModel,
                    shoppingCartViewModel = shoppingCartViewModel,
                    onBackButtonClick = onBackButtonClick,
                    showDialog = showDialog
                )
            }

    }
}





