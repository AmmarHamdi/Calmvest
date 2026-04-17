package com.calmvest.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.model.ReserveSummary
import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.model.User
import com.calmvest.core.domain.usecase.GetGoalsUseCase
import com.calmvest.core.domain.usecase.GetReserveSummaryUseCase
import com.calmvest.core.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val user: User?,
        val reserveSummary: ReserveSummary?,
        val goals: List<Goal>,
        val recentTransactions: List<Transaction>
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getGoalsUseCase: GetGoalsUseCase,
    private val getReserveSummaryUseCase: GetReserveSummaryUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val userId = "demo-user-id"

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        _uiState.update { DashboardUiState.Loading }
        viewModelScope.launch {
            var goals: List<Goal> = emptyList()
            var reserveSummary: ReserveSummary? = null
            var recentTransactions: List<Transaction> = emptyList()
            var errorMessage: String? = null

            getGoalsUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { goals = it },
                    onFailure = { errorMessage = it.message }
                )
            }

            getReserveSummaryUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { reserveSummary = it },
                    onFailure = { if (errorMessage == null) errorMessage = it.message }
                )
            }

            getTransactionsUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { recentTransactions = it.take(5) },
                    onFailure = { if (errorMessage == null) errorMessage = it.message }
                )
            }

            if (errorMessage != null && goals.isEmpty() && reserveSummary == null) {
                _uiState.update { DashboardUiState.Error(errorMessage ?: "Unknown error") }
            } else {
                _uiState.update {
                    DashboardUiState.Success(
                        user = null,
                        reserveSummary = reserveSummary,
                        goals = goals,
                        recentTransactions = recentTransactions
                    )
                }
            }
        }
    }
}
