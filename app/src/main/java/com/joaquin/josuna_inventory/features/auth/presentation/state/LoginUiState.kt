package com.joaquin.josuna_inventory.features.auth.presentation.state

import android.content.Intent

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val googleSignInIntent: Intent? = null
)
