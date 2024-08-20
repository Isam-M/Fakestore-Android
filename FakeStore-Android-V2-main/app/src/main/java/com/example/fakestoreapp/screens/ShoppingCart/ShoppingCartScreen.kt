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
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.fakestoreapp.utilities.LoadingAnimation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.design.AppTheme
import com.example.fakestoreapp.repositories.CartRepository
import com.example.fakestoreapp.screens.OrderHistory.OrderHistoryViewModel
import com.example.fakestoreapp.screens.Shared.BackButton
import com.example.fakestoreapp.screens.Shared.OrderHistoryButton
import com.example.fakestoreapp.screens.Shared.ShoppingCartButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingCartScreen(viewModel: ShoppingCartViewModel,orderHistoryViewModel: OrderHistoryViewModel, navController: NavController) {

    val cartItemsState by viewModel.cartItemsState.collectAsState()


    LaunchedEffect(key1 = Unit) {
        viewModel.initializeOrRefreshCart()
    }


AppTheme {

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            BackButton(navController)


            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Shopping Cart",
                style = MaterialTheme.typography.titleLarge
                    .copy(color = MaterialTheme.colorScheme.primary)

            )

            Spacer(modifier = Modifier.weight(1f))


            ShoppingCartButton(navController)
            OrderHistoryButton(navController)

        }
        CartScreenContent(
            cartItemsState,
            viewModel,
            orderHistoryViewModel
        )


    }
}
}
