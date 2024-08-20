package com.example.fakestoreapp.repositories

import android.content.Context
import androidx.room.Room
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.data.room.AppDatabase
import com.example.fakestoreapp.services.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.fakestoreapp.data.room.ProductDao
import kotlinx.coroutines.withContext

object ProductRepository {

    private val httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    private val retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val productService = retrofit.create(ProductService::class.java)
    private lateinit var _appDatabase: AppDatabase
    private val _productDao by lazy { _appDatabase.productDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app-database"
        ).fallbackToDestructiveMigration().build()
    }

    suspend fun getProducts(): List<Product> {
        println("getProducts function started")

        return try {
            val response = productService.getAllProducts()

            if (response.isSuccessful) {
                val productList = response.body() ?: emptyList()

                if (productList.isNotEmpty()) {
                    println("Received products from network:")
                    productList.forEach { product ->
                        println("  - $product")
                    }

                    val localProducts = withContext(Dispatchers.IO) {
                        _productDao.getAllProducts()
                    }

                    val favoriteProductsMap = localProducts.filter { it.favorite }.associateBy { it.id }

                    productList.forEach { product ->
                        val localFavoriteProduct = favoriteProductsMap[product.id]
                        product.favorite = localFavoriteProduct?.favorite ?: false
                    }


                    CoroutineScope(Dispatchers.IO).launch {
                        _productDao.insertProducts(productList)
                    }
                } else {
                    println("Received empty product list from network")
                }

                productList
            } else {
                println("error: ${response.code()} - ${response.message()}")

                val localProducts = withContext(Dispatchers.IO) {
                    _productDao.getAllProducts()
                }

                if (localProducts.isNotEmpty()) {
                    println("Using local products:")
                    localProducts.forEach { product ->
                        println("  - $product")
                    }
                    localProducts
                } else {
                    println("No products available")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            println("error: ${e.message}")

            val localProducts = withContext(Dispatchers.IO) {
                _productDao.getAllProducts()
            }

            if (localProducts.isNotEmpty()) {
                println("Using local products:")
                localProducts.forEach { product ->
                    println("  - $product")
                }
                localProducts
            } else {
                println("No products available")
                emptyList()
            }
        }
    }


    suspend fun getProductCategories(): List<String> {
        println("getProductCategories function started")

        return try {
            val response = productService.getProductCategories()
            if (response.isSuccessful) {
                val networkCategories = response.body() ?: emptyList()

                if (networkCategories.isNotEmpty()) {
                    println("Received product categories from network:")
                    networkCategories.forEach { productCategory ->
                        println("  - $productCategory")
                    }
                    networkCategories
                } else {
                    getLocalCategories()
                }
            } else {
                getLocalCategories()
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            getLocalCategories()
        }
    }

    private suspend fun getLocalCategories(): List<String> {
        return try {
            val localProducts = withContext(Dispatchers.IO) {
                _productDao.getAllProducts()
            }

            if (localProducts.isNotEmpty()) {
                val productCategories = localProducts.map { it.category }.distinct()

                if (productCategories.isNotEmpty()) {
                    println("Received product categories from database:")
                    productCategories.forEach { productCategory ->
                        println("  - $productCategory")
                    }
                } else {
                    println("No categories available in database")
                }

                productCategories
            } else {
                println("No local products available")
                emptyList()
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            emptyList()
        }
    }


    // get products by id
    suspend fun getProductById(
        productId: Int,
        coroutineScope: CoroutineScope,
        result: (Product) -> Unit
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val product = _productDao.getProductById(productId)
            result(product)
        }
    }


    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            _productDao.updateProduct(product)
        }
    }

    suspend fun getFavoriteProducts(): List<Product> {
        return try {
            val localFavoriteProducts = withContext(Dispatchers.IO) {
                _productDao.getFavoriteProducts()
            }

            if (localFavoriteProducts.isNotEmpty()) {
                println("Received favorite products from database:")
                localFavoriteProducts.forEach { favoriteProduct ->
                    println("  - $favoriteProduct")
                }
                localFavoriteProducts
            } else {
                println("No favorite products available in database")
                emptyList()
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            emptyList()
        }
    }



}


