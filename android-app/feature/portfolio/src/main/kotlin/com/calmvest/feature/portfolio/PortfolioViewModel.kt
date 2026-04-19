package com.calmvest.feature.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.domain.usecase.GetPortfolioUseCase
import com.calmvest.core.domain.usecase.TriggerManualInvestmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PortfolioUiState {
    data object Loading : PortfolioUiState
    data class Success(
        val portfolio: Portfolio,
        val orders: List<InvestmentOrder>,
        val isInvesting: Boolean = false
    ) : PortfolioUiState
    data class Error(val message: String) : PortfolioUiState
}

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val triggerManualInvestmentUseCase: TriggerManualInvestmentUseCase
) : ViewModel() {

    private val userId = "demo-user-id"

    private val _uiState = MutableStateFlow<PortfolioUiState>(PortfolioUiState.Loading)
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    private val _investMessage = MutableStateFlow<String?>(null)
    val investMessage: StateFlow<String?> = _investMessage.asStateFlow()

    init {
        loadPortfolio()
    }

    fun loadPortfolio() {
        _uiState.update { PortfolioUiState.Loading }
        viewModelScope.launch {
            getPortfolioUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { portfolio ->
                        _uiState.update {
                            PortfolioUiState.Success(
                                portfolio = portfolio,
                                orders = emptyList()
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update { PortfolioUiState.Error(it.message ?: "Failed to load portfolio") }
                    }
                )
            }
        }
    }

    fun triggerInvestment() {
        val current = _uiState.value as? PortfolioUiState.Success ?: return
        _uiState.update { current.copy(isInvesting = true) }
        viewModelScope.launch {
            triggerManualInvestmentUseCase(userId).fold(
                onSuccess = { order ->
                    _investMessage.update { "Investment of ${order.amount.formatEuros()} triggered!" }
                    _uiState.update { current.copy(isInvesting = false) }
                    loadPortfolio()
                },
                onFailure = {
                    _investMessage.update { it.message ?: "Investment failed" }
                    _uiState.update { current.copy(isInvesting = false) }
                }
            )
        }
    }

    fun clearInvestMessage() {
        _investMessage.update { null }
    }
}
