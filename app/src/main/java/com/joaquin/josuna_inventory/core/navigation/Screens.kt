package com.joaquin.josuna_inventory.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object InventoryHome

@Serializable
object AddProduct

@Serializable
object Statistics

@Serializable
object Alerts

@Serializable
object Profile

@Serializable
data class EditProduct(val productId: String)

@Serializable
object Camera
