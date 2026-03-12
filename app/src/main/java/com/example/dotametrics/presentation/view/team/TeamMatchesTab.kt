package com.example.dotametrics.presentation.view.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.teams.matches.TeamMatchesResult
import com.example.dotametrics.util.Datetime

@Composable
fun TeamMatchesScreen(
    viewModel: TeamViewModel,
    onMatchClick: (TeamMatchesResult) -> Unit
) {
    val team by viewModel.team.observeAsState()
    val matches by viewModel.matches.observeAsState()

    LaunchedEffect(team) {
        if (team != null && matches == null) {
            viewModel.loadMatches()
        }
    }

    if (matches == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(matches!!) { match ->
                TeamMatchItem(match = match, onClick = { onMatchClick(match) })
            }
        }
    }
}

@Composable
fun TeamMatchItem(match: TeamMatchesResult, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = match.opposingTeamLogo,
                    contentDescription = match.opposingTeamName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit,
                    error = painterResource(id = R.drawable.ic_box)
                )
                
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(
                        text = match.opposingTeamName ?: "Unknown",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = match.leagueName ?: "",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                
                val resultText = if (match.radiant != null && match.radiantWin != null) {
                    if (match.radiant!! && match.radiantWin!! || !match.radiant!! && !match.radiantWin!!)
                        stringResource(id = R.string.win)
                    else stringResource(id = R.string.lose)
                } else ""
                
                val resultColor = if (resultText == stringResource(id = R.string.win)) {
                    colorResource(id = R.color.green)
                } else {
                    colorResource(id = R.color.red)
                }
                
                Text(
                    text = resultText,
                    color = resultColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray, thickness = 0.5.dp)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.startTime?.let { Datetime.formatDate(it) } ?: "",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val radiantColor = when (match.radiantWin) {
                        true -> colorResource(id = R.color.green)
                        false -> colorResource(id = R.color.red)
                        else -> Color.White
                    }
                    val direColor = when (match.radiantWin) {
                        false -> colorResource(id = R.color.green)
                        true -> colorResource(id = R.color.red)
                        else -> Color.White
                    }

                    if (match.radiant == true) {
                        Text(text = match.radiantScore?.toString() ?: "0", color = radiantColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = " : ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = match.direScore?.toString() ?: "0", color = direColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    } else {
                        Text(text = match.direScore?.toString() ?: "0", color = direColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = " : ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = match.radiantScore?.toString() ?: "0", color = radiantColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                
                Text(
                    text = match.duration?.let { Datetime.getStringTime(it, context) } ?: "",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
