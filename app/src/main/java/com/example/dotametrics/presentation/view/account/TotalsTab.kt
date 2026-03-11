package com.example.dotametrics.presentation.view.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dotametrics.domain.entity.remote.players.totals.TotalsResult
import java.util.Locale

@Composable
fun TotalsScreen(viewModel: AccountViewModel) {
    val totals by viewModel.totals.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadTotals()
    }

    if (totals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(totals) { total ->
                TotalItem(total)
            }
        }
    }
}

@Composable
fun TotalItem(total: TotalsResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fieldName = total.field?.replace("_", " ")?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: ""
            Text(
                text = fieldName,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            
            // Calculating average: sum / n
            val averageValue = if (total.n != null && total.n!! > 0 && total.sum != null) {
                total.sum!! / total.n!!
            } else 0.0
            
            val displayValue = if (averageValue % 1 == 0.0) {
                averageValue.toLong().toString()
            } else {
                String.format(Locale.getDefault(), "%.2f", averageValue)
            }

            Text(
                text = displayValue,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Total: ${total.sum?.toLong() ?: 0} (${total.n ?: 0} matches)",
                color = Color.DarkGray,
                fontSize = 10.sp
            )
        }
    }
}
