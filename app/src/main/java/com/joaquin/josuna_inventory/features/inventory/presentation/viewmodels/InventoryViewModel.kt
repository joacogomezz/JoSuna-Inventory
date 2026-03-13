package com.joaquin.josuna_inventory.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.DeleteProductUseCase
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.GetProductsUseCase
import com.joaquin.josuna_inventory.features.inventory.presentation.state.InventoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val authRepository: AuthRepository,
    private val inventoryRepository: InventoryRepository,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        syncAndLoad()
    }

    private fun syncAndLoad() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                inventoryRepository.syncProducts()
            } catch (e: Exception) {
                // Continúa con Room local
            }
            getProductsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { products ->
                    _uiState.update { it.copy(
                        products = products,
                        filteredProducts = products,
                        isLoading = false
                    ) }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) {
                state.products
            } else {
                state.products.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }
            state.copy(searchQuery = query, filteredProducts = filtered)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                inventoryRepository.syncProducts()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
            vibratorManager.vibrateOnSave()
            soundManager.playSaveSound()
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            try {
                deleteProductUseCase(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
