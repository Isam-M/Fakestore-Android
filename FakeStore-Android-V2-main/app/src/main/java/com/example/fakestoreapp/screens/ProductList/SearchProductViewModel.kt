package com.example.fakestoreapp.screens.ProductList

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchProductViewModel : ViewModel() {
    private val _searchTerm = MutableStateFlow("")
    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())

    val searchTerm = _searchTerm.asStateFlow()
    val filteredProducts = _filteredProducts.asStateFlow()

    init {
        observeSearchTermChanges()
    }

    private fun observeSearchTermChanges() {
        viewModelScope.launch {
            searchTerm.collect { searchTerm ->
                filterProducts(searchTerm)
            }
        }
    }

    private fun filterProducts(searchTerm: String) {
        viewModelScope.launch {
            val allProducts = ProductRepository.getProducts()
            val filteredProducts = allProducts.filter { it.title.contains(searchTerm, ignoreCase = true) }
            _filteredProducts.value = filteredProducts
        }
    }

    fun updateSearchTerm(searchTerm: String) {
        println("Updating search term: $searchTerm")
        _searchTerm.value = searchTerm
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductComposable(searchProductViewModel: SearchProductViewModel, productListViewModel: ProductListViewModel) {
    val searchTermState = searchProductViewModel.searchTerm.collectAsState().value
    val filteredProductsState = searchProductViewModel.filteredProducts.collectAsState().value


    Column {
        OutlinedTextField(
            value = searchTermState,
            onValueChange = { newSearchTerm ->
                searchProductViewModel.updateSearchTerm(newSearchTerm)
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                println("Search button clicked with term: $searchTermState")
                searchProductViewModel.updateSearchTerm(searchTermState)
                productListViewModel.loadProducts()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Search")
        }

        Text(text = "Filtered Products: ${filteredProductsState.joinToString { it.title }}")
    }
}
