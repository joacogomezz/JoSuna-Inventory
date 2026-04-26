package com.joaquin.josuna_inventory.features.profile.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import com.joaquin.josuna_inventory.core.preferences.ProfilePreferences
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import com.joaquin.josuna_inventory.features.profile.domain.usecases.GetUserProfileUseCase
import com.joaquin.josuna_inventory.features.profile.presentation.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val authRepository: AuthRepository,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager,
    private val profilePreferences: ProfilePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
        loadLocalPhoto()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val profile = getUserProfileUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    profile = profile,
                    error = if (profile == null) "No se pudo cargar el perfil" else null
                )
            }
        }
    }

    private fun loadLocalPhoto() {
        viewModelScope.launch {
            profilePreferences.profilePhotoUri.collect { uri ->
                _uiState.update { it.copy(localPhotoUri = uri ?: "") }
            }
        }
    }

    fun onPhotoSelected(uri: Uri) {
        viewModelScope.launch {
            profilePreferences.setProfilePhotoUri(uri.toString())
            vibratorManager.vibrateOnSave()
            soundManager.playSaveSound()
            _uiState.update { it.copy(localPhotoUri = uri.toString()) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            vibratorManager.vibrateOnDelete()
            soundManager.playDeleteSound()
            authRepository.logout()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}
