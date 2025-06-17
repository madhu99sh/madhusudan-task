package com.sh.madhusudanshtask.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.sh.madhusudanshtask.domain.model.Holding
import com.sh.madhusudanshtask.presenter.viewmodel.HoldingViewModel
import java.text.DecimalFormat
import kotlin.math.abs

@Composable
fun HoldingScreen(viewModel: HoldingViewModel = hiltViewModel()) {


    val uiState by viewModel.uiState.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val isConnected by viewModel.isConnected.collectAsState()
    val isOfflineData = uiState is HoldingUiState.Success && !isConnected

    val currentRefreshCallback by rememberUpdatedState(viewModel::refreshHoldings)


    Column {
        if (isOfflineData) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFE082))
                    .padding(8.dp)
            ) {
                Text(
                    text = "You are offline. Showing cached data.",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (uiState) {
                is HoldingUiState.Loading -> CircularProgressIndicator()

                is HoldingUiState.Success -> {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing),
                        onRefresh = currentRefreshCallback
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 80.dp)
                        ) {
                            items(items = (uiState as HoldingUiState.Success).holdings,
                                key = { it.symbol }) { holding ->
                                HoldingCard(holding)
                            }
                        }
                        summary?.let {
                            
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                            ) {
                                HoldingSummaryContent(
                                    summary = it,
                                )
                            }
                        }
                    }
                }

                is HoldingUiState.Error -> Text(
                    "Error: ${(uiState as HoldingUiState.Error).message}",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}


@Composable
fun HoldingCard(holding: Holding) {
    val pnl by remember(holding) {
        derivedStateOf { holding.findTodayPnL() }
    }
    val pnlColor = if (pnl >= 0) Color(0xFF388E3C) else Color(0xFFD32F2F)
    val currencyFormatter = remember { DecimalFormat("₹#,##0.00") }

    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = holding.symbol,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            KeyValueText("LTP:", currencyFormatter.format(holding.ltp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyValueText("NET QTY:", holding.quantity.toString(), labelColor = Color.Gray)
            KeyValueText(
                label = "P&L:",
                value = "${if (pnl < 0) "-" else ""}${currencyFormatter.format(abs(pnl))}",
                valueColor = pnlColor
            )
        }
    }
    Divider()
}


@Composable
fun KeyValueText(
    label: String,
    value: String,
    labelColor: Color = Color.Gray,
    valueColor: Color = Color.Unspecified
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label ", color = labelColor, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value, color = valueColor, style = MaterialTheme.typography.bodyMedium
        )
    }
}




