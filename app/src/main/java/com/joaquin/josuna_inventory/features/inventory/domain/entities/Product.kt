package com.joaquin.josuna_inventory.features.inventory.domain.entities

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val photoPath: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

