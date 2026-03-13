package com.joaquin.josuna_inventory.features.inventory.data.mapper

import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.entity.ProductEntity
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
    quantity = quantity,
    photoPath = photoPath,
    createdAt = createdAt
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    price = price,
    quantity = quantity,
    photoPath = photoPath,
    createdAt = createdAt
)

fun Product.toDto(): Map<String, Any> = mapOf(
    "id" to id,
    "name" to name,
    "price" to price,
    "quantity" to quantity,
    "photoPath" to photoPath,
    "createdAt" to createdAt
)

fun Map<String, Any>.toDomain(): Product = Product(
    id = this["id"] as? String ?: "",
    name = this["name"] as? String ?: "",
    price = (this["price"] as? Number)?.toDouble() ?: 0.0,
    quantity = (this["quantity"] as? Number)?.toInt() ?: 0,
    photoPath = this["photoPath"] as? String ?: "",
    createdAt = (this["createdAt"] as? Number)?.toLong() ?: 0L
)

