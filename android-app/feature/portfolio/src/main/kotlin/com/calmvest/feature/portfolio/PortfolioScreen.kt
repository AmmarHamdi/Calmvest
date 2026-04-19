package com.calmvest.feature.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.calmvest.core.domain.model.InvestmentOrder
import com.calmvest.core.domain.model.OrderStatus
import com.calmvest.core.domain.model.Portfolio
import com.calmvest.core.ui.components.CalmCard
import com.calmvest.core.ui.components.ErrorScreen
import com.calmvest.core.ui.components.GainLossText
import com.calmvest.core.ui.components.LoadingScreen
import com.calmvest.core.ui.components.MoneyText
import com.calmvest.core.ui.theme.CalmNegative
import com.calmvest.core.ui.theme.CalmPositive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val investMessage by viewModel.investMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(investMessage) {
        investMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearInvestMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Portfolio") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (val state = uiState) {
            is PortfolioUiState.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            is PortfolioUiState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadPortfolio() },
                modifier = Modifier.padding(paddingValues)
            )
            is PortfolioUiState.Success -> PortfolioContent(
                state = state,
                onInvestNow = { viewModel.triggerInvestment() },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun PortfolioContent(
    state: PortfolioUiState.Success,
    onInvestNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item { PortfolioSummaryCard(portfolio = state.portfolio) }

        item {
            Button(
                onClick = onInvestNow,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !state.isInvesting
            ) {
                if (state.isInvesting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Invest Now", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        if (state.orders.isNotEmpty()) {
            item {
                Text(
                    text = "Investment Orders",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(state.orders) { order ->
                InvestmentOrderItem(order = order)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun PortfolioSummaryCard(portfolio: Portfolio) {
    CalmCard {
        // Mode badge
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
        ) {
            Text(
                text = "${portfolio.modeEmoji} ${portfolio.modeLabel}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Current Value",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        MoneyText(
            money = portfolio.currentValue,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total Invested",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                MoneyText(
                    money = portfolio.totalInvested,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Gain / Loss",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (portfolio.isGain) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                        contentDescription = null,
                        tint = if (portfolio.isGain) CalmPositive else CalmNegative,
                        modifier = Modifier.size(16.dp)
                    )
                    GainLossText(
                        money = portfolio.gainLoss,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = "%.2f%%".format(portfolio.gainLossPercent),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (portfolio.isGain) CalmPositive else CalmNegative,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun InvestmentOrderItem(order: InvestmentOrder) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = order.mode.let {
            when (it) {
                com.calmvest.core.domain.model.InvestmentMode.SAFE -> "💰"
                com.calmvest.core.domain.model.InvestmentMode.BITCOIN -> "₿"
                com.calmvest.core.domain.model.InvestmentMode.DIVERSIFIED -> "🌍"
            }
        }, fontSize = 24.sp)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = order.mode.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = order.createdAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            MoneyText(
                money = order.amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            val statusColor = when (order.status) {
                OrderStatus.EXECUTED -> CalmPositive
                OrderStatus.PENDING -> MaterialTheme.colorScheme.onSurfaceVariant
                OrderStatus.CANCELLED, OrderStatus.FAILED -> CalmNegative
            }
            Text(
                text = order.status.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = statusColor
            )
        }
    }
}
