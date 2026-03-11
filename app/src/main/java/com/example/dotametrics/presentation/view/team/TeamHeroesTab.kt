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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.teams.heroes.TeamHeroesResult
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.UrlConstants

@Composable
fun TeamHeroesScreen(
    viewModel: TeamViewModel,
    constViewModel: ConstViewModel,
    onHeroClick: (HeroResult) -> Unit
) {
    val team by viewModel.team.observeAsState()
    val heroes by viewModel.heroes.observeAsState()
    val allHeroes by constViewModel.heroes.observeAsState(emptyList())

    LaunchedEffect(team) {
        if (team != null && heroes == null) {
            viewModel.loadHeroes()
        }
    }
    
    // Ensure heroes are loaded for lookup
    LaunchedEffect(Unit) {
        constViewModel.loadHeroes()
    }

    if (heroes == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(heroes!!) { teamHero ->
                val heroInfo = allHeroes.firstOrNull { it.id == teamHero.heroId }
                TeamHeroItem(
                    teamHero = teamHero,
                    heroName = heroInfo?.localizedName,
                    heroImageName = heroInfo?.name,
                    onClick = { heroInfo?.let { onHeroClick(it) } }
                )
            }
        }
    }
}

@Composable
fun TeamHeroItem(teamHero: TeamHeroesResult, heroName: String?, heroImageName: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = if (heroImageName != null) {
                "${UrlConstants.HEROES_URL}/${heroImageName.replace(UrlConstants.HEROES_URL_REPLACE, "")}.png"
            } else null

            AsyncImage(
                model = imageUrl,
                contentDescription = heroName,
                modifier = Modifier
                    .size(60.dp, 45.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_hero)
            )

            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = heroName ?: "Unknown Hero",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Games", color = Color.Gray, fontSize = 10.sp)
                        Text(text = teamHero.gamesPlayed?.toString() ?: "0", color = Color.White, fontSize = 12.sp)
                    }
                    Column {
                        Text(text = "Wins", color = Color.Gray, fontSize = 10.sp)
                        Text(text = teamHero.wins?.toString() ?: "0", color = colorResource(id = R.color.green), fontSize = 12.sp)
                    }
                    Column {
                        Text(text = "Loses", color = Color.Gray, fontSize = 10.sp)
                        val loses = if (teamHero.gamesPlayed != null && teamHero.wins != null) teamHero.gamesPlayed!! - teamHero.wins!! else 0
                        Text(text = loses.toString(), color = colorResource(id = R.color.red), fontSize = 12.sp)
                    }
                    Column {
                        Text(text = "Winrate", color = Color.Gray, fontSize = 10.sp)
                        val winrate = if (teamHero.gamesPlayed != null && teamHero.wins != null && teamHero.gamesPlayed!! > 0) {
                            teamHero.wins!!.toDouble() / teamHero.gamesPlayed!! * 100
                        } else 0.0
                        Text(text = stringResource(id = R.string.percent_format, winrate), color = Color.Yellow, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
