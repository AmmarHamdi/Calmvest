package com.calmvest.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.domain.model.Money
import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.model.ReserveSummary
import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.model.User
import com.calmvest.core.domain.usecase.GetGoalsUseCase
import com.calmvest.core.domain.usecase.GetPortfolioUseCase
import com.calmvest.core.domain.usecase.GetReserveSummaryUseCase
import com.calmvest.core.domain.usecase.GetRoundUpRuleUseCase
import com.calmvest.core.domain.usecase.GetTransactionsUseCase
import com.calmvest.core.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
        val recentTransactions: List<Transaction>,
        val portfolio: Portfolio?,
        val autoInvestEnabled: Boolean,
        val lastRoundUp: Money?
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val getReserveSummaryUseCase: GetReserveSummaryUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val getRoundUpRuleUseCase: GetRoundUpRuleUseCase
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
            val userDeferred = async {
                var result: User? = null
                getUserUseCase(userId).collect { r -> r.onSuccess { result = it } }
                result
            }
            val goalsDeferred = async {
                var result: List<Goal> = emptyList()
                getGoalsUseCase(userId).collect { r -> r.onSuccess { result = it } }
                result
            }
            val reserveDeferred = async {
                var result: ReserveSummary? = null
                getReserveSummaryUseCase(userId).collect { r -> r.onSuccess { result = it } }
                result
            }
            val transactionsDeferred = async {
                var result: List<Transaction> = emptyList()
                getTransactionsUseCase(userId).collect { r -> r.onSuccess { result = it.take(5) } }
                result
            }
            val portfolioDeferred = async {
                var result: Portfolio? = null
                getPortfolioUseCase(userId).collect { r -> r.onSuccess { result = it } }
                result
            }
            val roundUpRuleDeferred = async {
                var enabled = false
                getRoundUpRuleUseCase(userId).collect { r -> r.onSuccess { enabled = it.isEnabled } }
                enabled
            }

            val user = userDeferred.await()
            val goals = goalsDeferred.await()
            val reserveSummary = reserveDeferred.await()
            val recentTransactions = transactionsDeferred.await()
            val portfolio = portfolioDeferred.await()
            val autoInvestEnabled = roundUpRuleDeferred.await()
            val lastRoundUp = recentTransactions
                .firstOrNull { it.isRoundedUp && it.roundUpAmount.minorUnits > 0 }
                ?.roundUpAmount

            _uiState.update {
                DashboardUiState.Success(
                    user = user,
                    reserveSummary = reserveSummary,
                    goals = goals,
                    recentTransactions = recentTransactions,
                    portfolio = portfolio,
                    autoInvestEnabled = autoInvestEnabled,
                    lastRoundUp = lastRoundUp
                )
            }
        }
    }
}
