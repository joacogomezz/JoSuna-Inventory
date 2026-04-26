package com.joaquin.josuna_inventory.features.profile.presentation.state

import com.joaquin.josuna_inventory.features.profile.domain.entities.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val error: String? = null,
    val isLoggedOut: Boolean = false,
    val localPhotoUri: String = ""
)
