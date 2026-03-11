package com.example.dotametrics.presentation.view.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.presentation.view.ConstViewModel
import kotlinx.coroutines.launch

@Composable
fun TeamScreen(
    viewModel: TeamViewModel,
    constViewModel: ConstViewModel,
    onPlayerClick: (com.example.dotametrics.domain.entity.remote.teams.players.TeamPlayersResult) -> Unit,
    onMatchClick: (com.example.dotametrics.domain.entity.remote.teams.matches.TeamMatchesResult) -> Unit,
    onHeroClick: (HeroResult) -> Unit
) {
    val team by viewModel.team.observeAsState()
    val scope = rememberCoroutineScope()
    
    val tabs = listOf(
        stringResource(id = R.string.tab_team_1),
        stringResource(id = R.string.tab_team_2),
        stringResource(id = R.string.tab_team_3)
    )
    
    val pagerState = rememberPagerState { tabs.size }

    Column(modifier = Modifier.fillMaxSize()) {
        team?.let { teamData ->
            TeamHeader(team = teamData)
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(id = R.color.purple_900),
            contentColor = Color.White,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color.White
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> TeamPlayersScreen(viewModel = viewModel, onPlayerClick = onPlayerClick)
                1 -> TeamMatchesScreen(viewModel = viewModel, onMatchClick = onMatchClick)
                2 -> TeamHeroesScreen(viewModel = viewModel, constViewModel = constViewModel, onHeroClick = onHeroClick)
            }
        }
    }
}

@Composable
fun TeamHeader(team: TeamsResult) {
    val headerBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.purple_700),
            colorResource(id = R.color.purple_900)
        )
    )

    Box(modifier = Modifier.fillMaxWidth().background(headerBrush)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = team.logoUrl,
                    contentDescription = team.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = team.name ?: "Unknown",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.team_rating, team.rating ?: 0),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        StatInfo(label = "Wins", value = team.wins?.toString() ?: "0", color = colorResource(id = R.color.green))
                        Spacer(modifier = Modifier.width(16.dp))
                        StatInfo(label = "Losses", value = team.losses?.toString() ?: "0", color = colorResource(id = R.color.red))
                    }
                }
            }
        }
    }

}

@Composable
fun StatInfo(label: String, value: String, color: Color) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
        Text(text = value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
