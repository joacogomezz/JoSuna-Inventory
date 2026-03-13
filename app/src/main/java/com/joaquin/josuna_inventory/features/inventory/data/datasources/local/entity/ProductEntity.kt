package com.joaquin.josuna_inventory.features.inventory.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val photoPath: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

