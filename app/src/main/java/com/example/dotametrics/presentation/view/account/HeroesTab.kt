package com.example.dotametrics.presentation.view.account

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.players.heroes.PlayerHeroResult
import com.example.dotametrics.presentation.view.ConstViewModel

@Composable
fun HeroesScreen(
    viewModel: AccountViewModel,
    constViewModel: ConstViewModel,
    onHeroClick: (HeroResult) -> Unit
) {
    val playerHeroes by viewModel.heroes.observeAsState(emptyList())
    val allHeroes by constViewModel.heroes.observeAsState(emptyList())

    val filteredHeroes = remember(playerHeroes) {
        playerHeroes.filter { (it.games ?: 0) > 0 }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (allHeroes.isEmpty()) constViewModel.loadHeroes()
        viewModel.loadPlayerHeroesResults()
    }

    if (playerHeroes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp, start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = filteredHeroes,
                key = { it.heroId ?: 0 }
            ) { playerHero ->
                val heroInfo = remember(playerHero.heroId, allHeroes) {
                    allHeroes.find { it.id == playerHero.heroId }
                }
                PlayerHeroItem(playerHero, heroInfo, onClick = {
                    heroInfo?.let { onHeroClick(it) }
                })
            }
        }
    }
}

@Composable
fun PlayerHeroItem(
    playerHero: PlayerHeroResult,
    heroInfo: HeroResult?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val heroImage = remember(heroInfo?.name) {
                heroInfo?.name?.replace("npc_dota_hero_", "")
            }
            AsyncImage(
                model = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/$heroImage.png",
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 34.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_hero)
            )

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(
                    text = heroInfo?.localizedName ?: "Unknown",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Last played: ${com.example.dotametrics.util.Datetime.formatDate(playerHero.lastPlayed ?: 0)}",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${playerHero.games} games",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                val winrate = if (playerHero.games != null && playerHero.games!! > 0) {
                    playerHero.win!!.toDouble() / playerHero.games!! * 100
                } else 0.0
                Text(
                    text = stringResource(id = R.string.percent_format, winrate),
                    color = Color.Yellow,
                    fontSize = 12.sp
                )
            }
        }
    }
}
