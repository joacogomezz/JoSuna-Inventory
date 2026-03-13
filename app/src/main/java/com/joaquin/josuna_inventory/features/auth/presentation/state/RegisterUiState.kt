package com.joaquin.josuna_inventory.features.auth.presentation.state

data class RegisterUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false
)
