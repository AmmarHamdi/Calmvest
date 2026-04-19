package com.calmvest.feature.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.calmvest.core.domain.model.Goal
import com.calmvest.core.ui.components.CalmButton
import com.calmvest.core.ui.components.CalmOutlinedButton
import com.calmvest.core.ui.components.ErrorScreen
import com.calmvest.core.ui.components.LoadingScreen
import com.calmvest.core.ui.components.MoneyText
import com.calmvest.core.ui.theme.CalmGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    goalId: String,
    onBack: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.goalDetailState.collectAsStateWithLifecycle()

    LaunchedEffect(goalId) {
        viewModel.loadGoal(goalId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Goal Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is GoalDetailUiState.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            is GoalDetailUiState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadGoal(goalId) },
                modifier = Modifier.padding(paddingValues)
            )
            is GoalDetailUiState.Success -> GoalDetailContent(
                goal = state.goal,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun GoalDetailContent(
    goal: Goal,
    modifier: Modifier = Modifier
) {
    var showPauseDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

    if (showPauseDialog) {
        ConfirmationDialog(
            title = "Pause this plan?",
            message = "Pausing will stop automatic round-ups for this goal. You can resume at any time.",
            confirmLabel = "Pause",
            onConfirm = { showPauseDialog = false },
            onDismiss = { showPauseDialog = false }
        )
    }

    if (showWithdrawDialog) {
        ConfirmationDialog(
            title = "Withdraw funds?",
            message = "Withdrawing moves your saved amount back to your bank account. Take a moment — is now the right time?",
            confirmLabel = "Withdraw",
            onConfirm = { showWithdrawDialog = false },
            onDismiss = { showWithdrawDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = goal.emoji, fontSize = 72.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = goal.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Circular progress
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { goal.progressFraction },
                modifier = Modifier.size(180.dp),
                strokeWidth = 14.dp,
                color = CalmGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${goal.progressPercent}%",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Amount details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AmountDetail(label = "Saved", money = goal.currentAmount)
            AmountDetail(label = "Remaining", money = goal.targetAmount - goal.currentAmount)
            AmountDetail(label = "Target", money = goal.targetAmount)
        }

        goal.targetDate?.let { date ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Target date: $date",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        CalmOutlinedButton(
            text = if (goal.isActive) "⏸  Pause Plan" else "▶  Resume Plan",
            onClick = { showPauseDialog = true }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CalmButton(
            text = "💸  Withdraw",
            onClick = { showWithdrawDialog = true }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AmountDetail(
    label: String,
    money: com.calmvest.core.domain.model.Money
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        MoneyText(
            money = money,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ConfirmationDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmLabel, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
