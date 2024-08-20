package com.example.fakestoreapp.screens.ProductDetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.data.Review
import com.example.fakestoreapp.repositories.ProductRepository
import com.example.fakestoreapp.repositories.ProductRepository.updateProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProductDetailsViewModel : ViewModel() {
    private val _selectedProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    val selectedProduct: StateFlow<Product?> get() = _selectedProduct

    private val productReviewsMap = mutableMapOf<Int, MutableStateFlow<List<Review>>>()

    fun addReview(productId: Int, review: Review) {
        val reviewWithProductId = review.copy(productId = productId)

        val reviewsForProduct = productReviewsMap.getOrPut(productId) {
            MutableStateFlow(emptyList())
        }

        val updatedReviews = (reviewsForProduct.value + reviewWithProductId)

        reviewsForProduct.value = updatedReviews

        println("Reviews for product $productId: $updatedReviews")
    }



    fun getReviewsForProduct(productId: Int): StateFlow<List<Review>> {
        return productReviewsMap[productId] ?: MutableStateFlow(emptyList())
    }

    fun setSelectedProduct(productId: Int) {
        viewModelScope.launch {
            try {
                ProductRepository.getProductById(productId, this) { product ->
                    _selectedProduct.value = product

                    productReviewsMap.getOrPut(productId) { MutableStateFlow(emptyList()) }
                }
            } catch (e: Exception) {
                println("Error fetching product: ${e.message}")
            }
        }
    }


    fun toggleFavorite() {
        viewModelScope.launch {
            selectedProduct.value?.let { product ->
                val updatedProduct = product.copy(favorite = !product.favorite)
                updateProduct(updatedProduct)
                _selectedProduct.value = updatedProduct
            }
        }
    }


}