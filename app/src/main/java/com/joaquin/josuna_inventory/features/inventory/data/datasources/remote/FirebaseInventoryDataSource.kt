package com.joaquin.josuna_inventory.features.inventory.data.datasources.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.joaquin.josuna_inventory.features.inventory.data.mapper.toDto
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseInventoryDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) {
    private fun getUserProductsRef() =
        database.getReference("users/${auth.currentUser?.uid}/products")

    fun getProducts(): Flow<List<Map<String, Any>>> = callbackFlow {
        val ref = getUserProductsRef()
        val listener = ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    @Suppress("UNCHECKED_CAST")
                    it.value as? Map<String, Any>
                }
                trySend(list)
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        })
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun addProduct(product: Product) {
        val ref = getUserProductsRef().child(product.id)
        ref.setValue(product.toDto()).await()
    }

    suspend fun updateProduct(product: Product) {
        val ref = getUserProductsRef().child(product.id)
        ref.setValue(product.toDto()).await()
    }

    suspend fun deleteProduct(id: String) {
        getUserProductsRef().child(id).removeValue().await()
    }
}

