package com.example.fakestoreapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("favorite")
    var favorite: Boolean,
    @SerializedName("category")
    val category: String,
    @Embedded
    @SerializedName("rating")
    val rating: ProductRating
)

data class ProductRating(
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("count")
    val count: Int
)

data class ProductListResponse(
    val results: List<Product>
)

data class Review(
    val productId: Int,
    val username: String,
    val rating: Double,
    val comment: String
)


