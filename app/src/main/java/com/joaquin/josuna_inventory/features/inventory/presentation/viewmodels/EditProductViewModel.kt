package com.joaquin.josuna_inventory.features.inventory.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joaquin.josuna_inventory.core.navigation.EditProduct
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.DeleteProductUseCase
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.GetProductsUseCase
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.UpdateProductUseCase
import com.joaquin.josuna_inventory.features.inventory.presentation.state.EditProductUiState
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProductUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val route = savedStateHandle.toRoute<EditProduct>()
        loadProduct(route.productId)
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val products = getProductsUseCase().first()
                val product = products.find { it.id == productId }
                if (product != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            id = product.id,
                            name = product.name,
                            price = product.price.toString(),
                            quantity = product.quantity.toString(),
                            photoPath = product.photoPath
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Producto no encontrado") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value, error = null) }
    fun onPriceChange(value: String) = _uiState.update { it.copy(price = value, error = null) }
    fun onQuantityChange(value: String) = _uiState.update { it.copy(quantity = value, error = null) }
    fun onPhotoPathChange(value: String) = _uiState.update { it.copy(photoPath = value) }

    fun updateProduct() {
        val state = _uiState.value
        val price = state.price.toDoubleOrNull()
        val quantity = state.quantity.toIntOrNull()
        if (state.name.isBlank() || price == null || quantity == null) {
            _uiState.update { it.copy(error = "Datos inválidos") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                updateProductUseCase(
                    Product(
                        id = state.id,
                        name = state.name.trim(),
                        price = price,
                        quantity = quantity,
                        photoPath = state.photoPath
                    )
                )
                vibratorManager.vibrateOnEdit()   // ← vibra más fuerte
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al actualizar") }
            }
        }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                deleteProductUseCase(_uiState.value.id)
                vibratorManager.vibrateOnDelete()  // ← vibra fuerte
                soundManager.playDeleteSound()      // ← sonido
                _uiState.update { it.copy(isLoading = false, isDeleted = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al eliminar") }
            }
        }
    }
}
