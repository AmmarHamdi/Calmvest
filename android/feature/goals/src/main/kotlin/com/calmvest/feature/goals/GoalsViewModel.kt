package com.calmvest.feature.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.usecase.GetGoalUseCase
import com.calmvest.core.domain.usecase.GetGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface GoalsUiState {
    data object Loading : GoalsUiState
    data class Success(val goals: List<Goal>) : GoalsUiState
    data class Error(val message: String) : GoalsUiState
}

sealed interface GoalDetailUiState {
    data object Loading : GoalDetailUiState
    data class Success(val goal: Goal) : GoalDetailUiState
    data class Error(val message: String) : GoalDetailUiState
}

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoalsUseCase: GetGoalsUseCase,
    private val getGoalUseCase: GetGoalUseCase
) : ViewModel() {

    private val userId = "demo-user-id"

    private val _goalsState = MutableStateFlow<GoalsUiState>(GoalsUiState.Loading)
    val goalsState: StateFlow<GoalsUiState> = _goalsState.asStateFlow()

    private val _goalDetailState = MutableStateFlow<GoalDetailUiState>(GoalDetailUiState.Loading)
    val goalDetailState: StateFlow<GoalDetailUiState> = _goalDetailState.asStateFlow()

    init {
        loadGoals()
    }

    fun loadGoals() {
        _goalsState.update { GoalsUiState.Loading }
        viewModelScope.launch {
            getGoalsUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { _goalsState.update { GoalsUiState.Success(it) } },
                    onFailure = { _goalsState.update { GoalsUiState.Error(it.message ?: "Failed to load goals") } }
                )
            }
        }
    }

    fun loadGoal(goalId: String) {
        _goalDetailState.update { GoalDetailUiState.Loading }
        viewModelScope.launch {
            getGoalUseCase(userId, goalId).collect { result ->
                result.fold(
                    onSuccess = { _goalDetailState.update { GoalDetailUiState.Success(it) } },
                    onFailure = { _goalDetailState.update { GoalDetailUiState.Error(it.message ?: "Failed to load goal") } }
                )
            }
        }
    }
}
