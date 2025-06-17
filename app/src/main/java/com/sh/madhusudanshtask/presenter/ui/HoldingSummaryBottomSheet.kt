package com.sh.madhusudanshtask.presenter.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sh.madhusudanshtask.domain.model.HoldingSummary
import java.text.DecimalFormat
import kotlin.math.abs


@Composable
fun SummaryRow(
    label: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.secondary,
    isExpandable: Boolean = false,
    expanded: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(color = color)
            )
            if (isExpandable) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


fun formatSigned(value: Double, formatter: DecimalFormat): String {
    return (if (value < 0) "-" else "") + formatter.format(abs(value))
}

@SuppressLint("DefaultLocale")
fun HoldingSummary.pnlPercent(): String {
    if (totalInvestment == 0.0) return "0.00%"
    val percent = (totalPNL / totalInvestment) * 100
    return String.format("%.2f%%", percent)
}


@Composable
fun HoldingSummaryContent(
    summary: HoldingSummary,
) {
    val currencyFormatter = remember { DecimalFormat("₹#,##0.00") }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SummaryRow("Current value*", currencyFormatter.format(summary.currentValue))
                    SummaryRow(
                        "Total investment*", currencyFormatter.format(summary.totalInvestment)
                    )
                    SummaryRow(
                        "Today’s Profit & Loss*",
                        formatSigned(summary.todayPNL, currencyFormatter),
                        color = if (summary.todayPNL >= 0) Color(0xFF388E3C) else Color(0xFFD32F2F)
                    )
                    Divider()
                }
            }

            SummaryRow(label = "Profit & Loss*",
                value = "${
                    formatSigned(
                        summary.totalPNL, currencyFormatter
                    )
                } (${summary.pnlPercent()}%)",
                color = if (summary.totalPNL >= 0) Color(0xFF388E3C) else Color(0xFFD32F2F),
                isExpandable = true,
                expanded = expanded,
                onClick = { expanded = !expanded })

        }
    }
}
