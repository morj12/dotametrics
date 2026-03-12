package com.example.dotametrics.presentation.view.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.teams.players.TeamPlayersResult

@Composable
fun TeamPlayersScreen(
    viewModel: TeamViewModel,
    onPlayerClick: (TeamPlayersResult) -> Unit
) {
    val team by viewModel.team.observeAsState()
    val players by viewModel.players.observeAsState()

    LaunchedEffect(team) {
        if (team != null && players == null) {
            viewModel.loadPlayers()
        }
    }

    if (players == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(players!!) { player ->
                TeamPlayerItem(player = player, onClick = { onPlayerClick(player) })
            }
        }
    }
}

@Composable
fun TeamPlayerItem(player: TeamPlayersResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = player.name ?: "Unknown",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Games", color = Color.Gray, fontSize = 12.sp)
                    Text(text = player.gamesPlayed?.toString() ?: "0", color = Color.White)
                }
                Column {
                    Text(text = "Wins", color = Color.Gray, fontSize = 12.sp)
                    Text(text = player.wins?.toString() ?: "0", color = colorResource(id = R.color.green))
                }
                Column {
                    Text(text = "Loses", color = Color.Gray, fontSize = 12.sp)
                    val loses = if (player.gamesPlayed != null && player.wins != null) player.gamesPlayed!! - player.wins!! else 0
                    Text(text = loses.toString(), color = colorResource(id = R.color.red))
                }
                Column {
                    Text(text = "Winrate", color = Color.Gray, fontSize = 12.sp)
                    val winrate = if (player.gamesPlayed != null && player.wins != null && player.gamesPlayed!! > 0) {
                        player.wins!!.toDouble() / player.gamesPlayed!! * 100
                    } else 0.0
                    Text(text = stringResource(id = R.string.percent_format, winrate), color = Color.Yellow)
                }
            }
        }
    }
}
