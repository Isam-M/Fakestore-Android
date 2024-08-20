package com.example.fakestoreapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fakestoreapp.data.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: Int): Product

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)

    @Query("UPDATE product SET favorite = NOT favorite WHERE id = :productId")
    suspend fun toggleFavorite(productId: Int)

    @Query("SELECT * FROM product WHERE favorite = 1")
    suspend fun getFavoriteProducts(): List<Product>

}
