package com.calmvest.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.InvestmentMode
import com.calmvest.core.domain.model.RoundUpRule
import com.calmvest.core.domain.usecase.GetRoundUpRuleUseCase
import com.calmvest.core.domain.usecase.UpdateRoundUpRuleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val roundUpEnabled: Boolean = true,
    val monthlyCapMinorUnits: Long = 5000L,
    val thresholdMinorUnits: Long = 0L,
    val pauseUntil: String? = null,
    val investmentMode: InvestmentMode = InvestmentMode.SAFE,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getRoundUpRuleUseCase: GetRoundUpRuleUseCase,
    private val updateRoundUpRuleUseCase: UpdateRoundUpRuleUseCase
) : ViewModel() {

    private val userId = "demo-user-id"

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getRoundUpRuleUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { rule ->
                        _uiState.update {
                            it.copy(
                                roundUpEnabled = rule.isEnabled,
                                monthlyCapMinorUnits = rule.monthlyCapMinorUnits,
                                thresholdMinorUnits = rule.thresholdMinorUnits,
                                pauseUntil = rule.pauseUntil,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update { state ->
                            state.copy(isLoading = false, error = it.message)
                        }
                    }
                )
            }
        }
    }

    fun setRoundUpEnabled(enabled: Boolean) {
        _uiState.update { it.copy(roundUpEnabled = enabled) }
    }

    fun setMonthlyCap(capMinorUnits: Long) {
        _uiState.update { it.copy(monthlyCapMinorUnits = capMinorUnits) }
    }

    fun setThreshold(thresholdMinorUnits: Long) {
        _uiState.update { it.copy(thresholdMinorUnits = thresholdMinorUnits) }
    }

    fun setPauseUntil(date: String?) {
        _uiState.update { it.copy(pauseUntil = date) }
    }

    fun setInvestmentMode(mode: InvestmentMode) {
        _uiState.update { it.copy(investmentMode = mode) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun saveSettings() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true, saveSuccess = false, error = null) }
        viewModelScope.launch {
            updateRoundUpRuleUseCase(
                userId = userId,
                isEnabled = state.roundUpEnabled,
                monthlyCapMinorUnits = state.monthlyCapMinorUnits,
                thresholdMinorUnits = state.thresholdMinorUnits,
                pauseUntil = state.pauseUntil
            ).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, saveSuccess = true) } },
                onFailure = { throwable -> _uiState.update { it.copy(isLoading = false, error = throwable.message) } }
            )
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, saveSuccess = false) }
    }
}
