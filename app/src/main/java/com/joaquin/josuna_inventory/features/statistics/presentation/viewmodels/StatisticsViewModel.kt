package com.joaquin.josuna_inventory.features.statistics.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import com.joaquin.josuna_inventory.features.statistics.domain.usecases.GetInventoryStatsUseCase
import com.joaquin.josuna_inventory.features.statistics.presentation.state.StatisticsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getInventoryStatsUseCase: GetInventoryStatsUseCase,
    private val vibratorManager: VibratorManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            vibratorManager.vibrateOnSave()
            soundManager.playDeleteSound()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getInventoryStatsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { stats ->
                    vibratorManager.vibrateOnSave()
                    _uiState.update {
                        it.copy(stats = stats, isLoading = false, error = null)
                    }
                }
        }
    }
}
