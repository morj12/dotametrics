package com.example.dotametrics.presentation.view.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult
import com.example.dotametrics.domain.entity.remote.players.matches.MatchesResult
import com.example.dotametrics.domain.entity.remote.players.wl.WLResult
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.LobbyTypeMapper

@Composable
fun MatchesScreen(
    viewModel: AccountViewModel,
    constViewModel: ConstViewModel,
    onMatchClick: (Long) -> Unit
) {
    val matches: LazyPagingItems<MatchesResult> = viewModel.matchesFlow.collectAsLazyPagingItems()
    val heroes by constViewModel.heroes.observeAsState(emptyList())
    val lobbies by constViewModel.constLobbyTypes.observeAsState(emptyList())
    val filteredWl by viewModel.filteredWl.observeAsState()
    val context = LocalContext.current

    var showLobbyMenu by remember { mutableStateOf(false) }
    var showHeroMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (heroes.isEmpty()) constViewModel.loadHeroes()
        if (lobbies.isEmpty()) constViewModel.loadLobbyTypes()
        viewModel.loadMatches()
        viewModel.loadFilteredWLResults()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Lobby Filter
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { showLobbyMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    val currentLobby = lobbies.find { it.id == viewModel.lobbyType }
                    Text(
                        text = if (currentLobby == null) stringResource(id = R.string.any_lobby)
                        else stringResource(LobbyTypeMapper().getLobbyResource(currentLobby.name!!, context)),
                        maxLines = 1
                    )
                }
                DropdownMenu(
                    expanded = showLobbyMenu,
                    onDismissRequest = { showLobbyMenu = false },
                    modifier = Modifier.background(colorResource(id = R.color.purple_900))
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.any_lobby), color = Color.White) },
                        onClick = {
                            viewModel.lobbyType = null
                            viewModel.loadMatches()
                            viewModel.loadFilteredWLResults()
                            showLobbyMenu = false
                        }
                    )
                    lobbies.forEach { lobby ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = stringResource(LobbyTypeMapper().getLobbyResource(lobby.name!!, context)),
                                    color = Color.White
                                ) 
                            },
                            onClick = {
                                viewModel.lobbyType = lobby.id
                                viewModel.loadMatches()
                                viewModel.loadFilteredWLResults()
                                showLobbyMenu = false
                            }
                        )
                    }
                }
            }

            // Hero Filter
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { showHeroMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    val currentHero = heroes.find { it.id == viewModel.heroId }
                    Text(text = currentHero?.localizedName ?: "Hero", maxLines = 1)
                }
                DropdownMenu(
                    expanded = showHeroMenu,
                    onDismissRequest = { showHeroMenu = false },
                    modifier = Modifier.heightIn(max = 400.dp).background(colorResource(id = R.color.purple_900))
                ) {
                    DropdownMenuItem(
                        text = { Text("All Heroes", color = Color.White) },
                        onClick = {
                            viewModel.heroId = null
                            viewModel.loadMatches()
                            viewModel.loadFilteredWLResults()
                            showHeroMenu = false
                        }
                    )
                    heroes.forEach { hero ->
                        DropdownMenuItem(
                            text = { Text(hero.localizedName ?: "", color = Color.White) },
                            onClick = {
                                viewModel.heroId = hero.id
                                viewModel.loadMatches()
                                viewModel.loadFilteredWLResults()
                                showHeroMenu = false
                            }
                        )
                    }
                }
            }
        }

        // Filtered WL Info
        filteredWl?.let { wl ->
            FilteredWLInfo(wl)
        }

        // Matches List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                count = matches.itemCount,
                key = matches.itemKey { it.matchId ?: 0L }
            ) { index ->
                matches[index]?.let { match ->
                    MatchItem(match, heroes, lobbies, onMatchClick)
                }
            }

            when (val state = matches.loadState.refresh) {
                is LoadState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }
                is LoadState.Error -> {
                    item {
                        Text(text = "Error: ${state.error.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                }
                else -> {}
            }
            
            // Loading state for append
            if (matches.loadState.append is LoadState.Loading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilteredWLInfo(wl: WLResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatValue(label = "Wins", value = wl.win ?: 0, color = colorResource(id = R.color.green))
        StatValue(label = "Losses", value = wl.lose ?: 0, color = colorResource(id = R.color.red))
        val winrate = if (wl.win != null && wl.lose != null && (wl.win!! + wl.lose!!) > 0) {
            wl.win!!.toDouble() / (wl.win!! + wl.lose!!) * 100
        } else 0.0
        StatValue(label = "Winrate", value = stringResource(id = R.string.percent_format, winrate), color = Color.Yellow)
    }
}

@Composable
fun StatValue(label: String, value: Any, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
        Text(text = value.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun MatchItem(
    match: MatchesResult,
    heroes: List<com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult>,
    lobbies: List<LobbyTypeResult>,
    onClick: (Long) -> Unit
) {
    val hero = heroes.find { it.id == match.heroId }
    val lobby = lobbies.find { it.id == match.lobbyType }
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick(match.matchId ?: 0) },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val heroImage = hero?.name?.replace("npc_dota_hero_", "")
            AsyncImage(
                model = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/$heroImage.png",
                contentDescription = null,
                modifier = Modifier.size(60.dp, 34.dp),
                contentScale = ContentScale.Fit
            )
            
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(text = hero?.localizedName ?: "Unknown", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                val lobbyText = if (lobby != null) {
                    val resId = LobbyTypeMapper().getLobbyResource(lobby.name!!, context)
                    if (resId != 0) stringResource(resId) else lobby.name
                } else "Unknown"
                Text(text = lobbyText ?: "Unknown", color = Color.Gray, fontSize = 11.sp)
                Text(text = Datetime.formatDate(match.startTime ?: 0), color = Color.Gray, fontSize = 11.sp)
            }

            // Party and Rank info
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp)) {
                if (match.partySize != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(text = " x${match.partySize}", color = Color.Gray, fontSize = 11.sp)
                    }
                }
                if (match.averageRank != null && match.averageRank!! > 0) {
                    val rankResId = context.resources.getIdentifier("r${match.averageRank}", "drawable", context.packageName)
                    if (rankResId != 0) {
                        Image(
                            painter = painterResource(id = rankResId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(70.dp)) {
                val won = (match.playerSlot!! < 128 && match.radiantWin == true) || (match.playerSlot!! >= 128 && match.radiantWin == false)
                Text(
                    text = if (won) "WON" else "LOST",
                    color = if (won) colorResource(id = R.color.green) else colorResource(id = R.color.red),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(text = "${match.kills}/${match.deaths}/${match.assists}", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
