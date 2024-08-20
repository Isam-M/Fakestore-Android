package com.example.fakestoreapp.services


import com.example.fakestoreapp.data.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/categories")
    suspend fun getProductCategories(): Response<List<String>>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Response<Product>

}