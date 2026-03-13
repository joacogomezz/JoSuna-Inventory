package com.joaquin.josuna_inventory.features.inventory.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.dao.ProductDao
import com.joaquin.josuna_inventory.features.inventory.data.datasources.remote.FirebaseInventoryDataSource
import com.joaquin.josuna_inventory.features.inventory.data.mapper.toDomain
import com.joaquin.josuna_inventory.features.inventory.data.mapper.toEntity
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: FirebaseInventoryDataSource,
    private val productDao: ProductDao,
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : InventoryRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncProducts() {
        try {
            val uid = auth.currentUser?.uid ?: return
            val snapshot = database
                .getReference("users/$uid/products")
                .get()
                .await()

            val products = snapshot.children.mapNotNull { child ->
                @Suppress("UNCHECKED_CAST")
                (child.value as? Map<String, Any>)?.toDomain()
            }

            productDao.deleteAll()
            productDao.insertAll(products.map { it.toEntity() })
        } catch (e: Exception) {
            // Si no hay internet, Room ya tiene los datos cacheados
        }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insertProduct(product.toEntity())
        remoteDataSource.addProduct(product)
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
        remoteDataSource.updateProduct(product)
    }

    override suspend fun deleteProduct(id: String) {
        productDao.deleteProduct(id)
        remoteDataSource.deleteProduct(id)
    }
}
