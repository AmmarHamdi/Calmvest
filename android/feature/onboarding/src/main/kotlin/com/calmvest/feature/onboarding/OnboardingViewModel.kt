package com.calmvest.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.GoalType
import com.calmvest.core.domain.model.InvestmentMode
import com.calmvest.core.domain.usecase.CreateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val selectedGoalType: GoalType? = null,
    val goalName: String = "",
    val targetAmountInput: String = "",
    val targetDate: String? = null,
    val selectedMode: InvestmentMode = InvestmentMode.SAFE,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isComplete: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {

    // Demo userId — in a real app this would come from auth
    private val userId = "demo-user-id"

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun selectGoalType(type: GoalType) {
        _uiState.update { it.copy(selectedGoalType = type) }
    }

    fun updateGoalName(name: String) {
        _uiState.update { it.copy(goalName = name) }
    }

    fun updateTargetAmount(amount: String) {
        _uiState.update { it.copy(targetAmountInput = amount) }
    }

    fun updateTargetDate(date: String?) {
        _uiState.update { it.copy(targetDate = date) }
    }

    fun selectInvestmentMode(mode: InvestmentMode) {
        _uiState.update { it.copy(selectedMode = mode) }
    }

    fun createGoalAndFinish() {
        val state = _uiState.value
        val goalType = state.selectedGoalType ?: return
        val amountMinorUnits = state.targetAmountInput.toDoubleOrNull()?.let { (it * 100).toLong() } ?: return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = createGoalUseCase(
                userId = userId,
                name = state.goalName.ifBlank { goalType.name.replace("_", " ") },
                goalType = goalType,
                targetAmountMinorUnits = amountMinorUnits,
                targetDate = state.targetDate
            )
            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isComplete = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
