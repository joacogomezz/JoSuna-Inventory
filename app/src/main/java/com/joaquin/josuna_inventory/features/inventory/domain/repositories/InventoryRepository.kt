package com.joaquin.josuna_inventory.features.inventory.domain.repositories

import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getLocalProducts(): List<Product>
    suspend fun syncProducts()
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(id: String)
}
