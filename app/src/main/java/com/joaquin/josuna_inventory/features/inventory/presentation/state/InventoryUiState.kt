package com.joaquin.josuna_inventory.features.inventory.presentation.state

import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product

data class InventoryUiState(
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false, // ← nuevo
    val error: String? = null
)
