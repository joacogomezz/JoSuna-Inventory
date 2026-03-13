package com.joaquin.josuna_inventory.features.alerts.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import com.joaquin.josuna_inventory.features.alerts.domain.usecases.GetLowStockAlertsUseCase
import com.joaquin.josuna_inventory.features.alerts.presentation.state.AlertsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getLowStockAlertsUseCase: GetLowStockAlertsUseCase,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            getLowStockAlertsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { alerts ->
                    val hadAlerts = _uiState.value.alerts.isEmpty() && alerts.isNotEmpty()
                    _uiState.update { it.copy(alerts = alerts, isLoading = false) }
                    // Vibra y suena solo la primera vez que detecta alertas
                    if (hadAlerts) {
                        vibratorManager.vibrateOnDelete()
                        soundManager.playDeleteSound()
                    }
                }
        }
    }
}

