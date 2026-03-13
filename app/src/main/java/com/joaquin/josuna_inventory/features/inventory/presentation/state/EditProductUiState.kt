package com.joaquin.josuna_inventory.features.inventory.presentation.state

data class EditProductUiState(
    val isLoading: Boolean = false,
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val photoPath: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val isDeleted: Boolean = false
)

