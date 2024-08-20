package com.example.fakestoreapp.screens.ProductList

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProductListViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>("All Products")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    // Search ViewModel
    val searchProductViewModel = SearchProductViewModel()

    init {
        loadCategories()
        loadProducts()
    }

    fun onSearchTermChanged(searchTerm: String) {
        searchProductViewModel.updateSearchTerm(searchTerm)
    }

    private fun updateCategoriesWithAllProducts(categories: List<String>): List<String> {
        val allCategories = ArrayList(categories)
        allCategories.add(0, "All Products")
        allCategories.add("Favorites")
        return allCategories
    }

    fun loadCategories() {
        viewModelScope.launch {
            _loading.value = true
            val loadedCategories = ProductRepository.getProductCategories()

            if (loadedCategories.isNotEmpty()) {
                _categories.value = updateCategoriesWithAllProducts(loadedCategories)
                _selectedCategory.value = "All Products"
            } else {
                println("No categories found")
            }

            _loading.value = false
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val allProducts = if (selectedCategory.value == "Favorites") {
                    ProductRepository.getFavoriteProducts()
                } else {
                    ProductRepository.getProducts()
                }

                println("Selected category: ${selectedCategory.value}")
                println("Search term: ${searchProductViewModel.searchTerm.value}")

                val filteredProducts = if (selectedCategory.value == "All Products" || selectedCategory.value == "Favorites") {
                    allProducts
                } else {
                    allProducts.filter { it.category == selectedCategory.value }
                }

                val searchTerm = searchProductViewModel.searchTerm.value
                println("Filtered products before search: $filteredProducts")

                val searchFilteredProducts = if (searchTerm.isNullOrBlank()) {
                    filteredProducts
                } else {
                    filteredProducts.filter { it.title.contains(searchTerm, ignoreCase = true) }
                }

                _products.value = searchFilteredProducts
                println("Filtered products after search: $searchFilteredProducts")
            } finally {
                _loading.value = false
            }
        }
    }


    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadProducts()
    }
}

