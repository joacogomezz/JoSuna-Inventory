package com.joaquin.josuna_inventory.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.AddProductUseCase
import com.joaquin.josuna_inventory.features.inventory.presentation.state.AddProductUiState
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onPriceChange(value: String) = _uiState.update { it.copy(price = value, error = null) }
    fun onQuantityChange(value: String) = _uiState.update { it.copy(quantity = value, error = null) }
    fun onPhotoPathChange(value: String) = _uiState.update { it.copy(photoPath = value) }

    fun addProduct() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }
        val price = state.price.toDoubleOrNull()
        if (price == null || price < 0) {
            _uiState.update { it.copy(error = "Precio inválido") }
            return
        }
        val quantity = state.quantity.toIntOrNull()
        if (quantity == null || quantity < 0) {
            _uiState.update { it.copy(error = "Cantidad inválida") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                addProductUseCase(
                    Product(
                        id = UUID.randomUUID().toString(),
                        name = state.name.trim(),
                        price = price,
                        quantity = quantity,
                        photoPath = state.photoPath
                    )
                )
                vibratorManager.vibrateOnSave()   // ← vibra
                soundManager.playSaveSound()       // ← sonido
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al agregar") }
            }
        }
    }
}
