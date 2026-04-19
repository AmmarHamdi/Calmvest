package com.calmvest.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmvest.core.domain.model.Transaction
import com.calmvest.core.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TransactionFilter { ALL, ROUNDED_UP, THIS_MONTH }

sealed interface TransactionsUiState {
    data object Loading : TransactionsUiState
    data class Success(
        val transactions: List<Transaction>,
        val filter: TransactionFilter
    ) : TransactionsUiState
    data class Error(val message: String) : TransactionsUiState
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val userId = "demo-user-id"
    private var allTransactions: List<Transaction> = emptyList()

    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState.Loading)
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    private var currentFilter = TransactionFilter.ALL

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        _uiState.update { TransactionsUiState.Loading }
        viewModelScope.launch {
            getTransactionsUseCase(userId).collect { result ->
                result.fold(
                    onSuccess = { transactions ->
                        allTransactions = transactions
                        applyFilter(currentFilter)
                    },
                    onFailure = {
                        _uiState.update { TransactionsUiState.Error(it.message ?: "Failed to load transactions") }
                    }
                )
            }
        }
    }

    fun setFilter(filter: TransactionFilter) {
        currentFilter = filter
        applyFilter(filter)
    }

    private fun applyFilter(filter: TransactionFilter) {
        val filtered = when (filter) {
            TransactionFilter.ALL -> allTransactions
            TransactionFilter.ROUNDED_UP -> allTransactions.filter { it.isRoundedUp }
            TransactionFilter.THIS_MONTH -> {
                val thisMonth = java.time.YearMonth.now().toString()
                allTransactions.filter { it.date.startsWith(thisMonth) }
            }
        }
        _uiState.update { TransactionsUiState.Success(filtered, filter) }
    }
}
