package com.example.fakestoreapp.screens.ProductList


import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fakestoreapp.design.AppTheme
import com.example.fakestoreapp.screens.Shared.BackButton
import com.example.fakestoreapp.screens.Shared.Navbar
import com.example.fakestoreapp.screens.Shared.OrderHistoryButton
import com.example.fakestoreapp.screens.Shared.ShoppingCartButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit = {},
    navController: NavController

) {
    val loading = viewModel.loading.collectAsState()
    val products = viewModel.products.collectAsState()
    val selectedCategory = viewModel.selectedCategory.collectAsState()
    var isVisible by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val columns = if (isLandscape) 4 else 2


    if (loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .alpha(0.8f),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        return
    }
    AppTheme {

        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,

            )  {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BackButton(navController)
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Products",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.weight(1f))
                ShoppingCartButton(navController)
                OrderHistoryButton(navController)
                IconButton(
                    onClick = { isVisible = !isVisible },
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Toggle Search Bar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }


            AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier.padding(8.dp)
            ) {
                // Search bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = viewModel.searchProductViewModel.searchTerm.collectAsState().value,
                        onValueChange = { newSearchTerm ->
                            viewModel.onSearchTermChanged(newSearchTerm)
                        },
                        label = { Text("Search", color = MaterialTheme.colorScheme.onSurface) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Button(
                        onClick = {
                            val newSearchTerm = viewModel.searchProductViewModel.searchTerm.value
                            viewModel.searchProductViewModel.updateSearchTerm(newSearchTerm)
                            viewModel.loadProducts()
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text("Search")
                    }
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier.padding(8.dp)
            ) {
                // categories nav bar
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(viewModel.categories.value) { category ->
                        ProductCategoryItem(
                            category = category,
                            isSelected = category == selectedCategory.value,
                            onCategoryClick = { viewModel.selectCategory(category) }
                        )
                    }
                }
            }



            Divider()

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(columns),
                verticalItemSpacing = 8.dp,
            )
            {
                items(products.value) { product ->
                    ProductItem(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}


