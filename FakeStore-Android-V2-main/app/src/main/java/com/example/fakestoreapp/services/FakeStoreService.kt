package com.example.fakestoreapp.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeStoreService {

        private fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        }
    
        val apiService = getClient().create(ProductService::class.java)

}