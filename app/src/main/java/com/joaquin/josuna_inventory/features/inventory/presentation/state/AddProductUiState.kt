package com.joaquin.josuna_inventory.features.inventory.presentation.state

data class AddProductUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val photoPath: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false
)

