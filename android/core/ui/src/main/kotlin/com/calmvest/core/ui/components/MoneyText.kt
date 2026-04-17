package com.calmvest.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.calmvest.core.domain.model.Money
import com.calmvest.core.ui.theme.CalmNegative
import com.calmvest.core.ui.theme.CalmPositive

@Composable
fun MoneyText(
    money: Money,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight? = null,
    showSign: Boolean = false
) {
    val text = if (showSign && money.minorUnits > 0) "+${money.formatEuros()}"
               else money.formatEuros()
    Text(
        text = text,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight
    )
}

@Composable
fun GainLossText(
    money: Money,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val isPositive = money.minorUnits >= 0
    val color: Color = if (isPositive) CalmPositive else CalmNegative
    val sign = if (isPositive) "+" else ""
    Text(
        text = "$sign${money.formatEuros()}",
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun RoundUpAmountText(
    money: Money,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    Text(
        text = "+${money.formatEuros()}",
        modifier = modifier,
        style = style,
        color = CalmPositive,
        fontWeight = FontWeight.Medium
    )
}
