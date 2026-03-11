package com.example.dotametrics.presentation.view.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.players.PlayersResult
import com.example.dotametrics.domain.entity.remote.players.Profile
import com.example.dotametrics.domain.entity.remote.players.wl.WLResult
import com.example.dotametrics.presentation.view.ConstViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    constViewModel: ConstViewModel,
    onHeroClick: (com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult) -> Unit,
    onMatchClick: (Long) -> Unit,
    onPeerClick: (Long) -> Unit
) {
    val playerResult by viewModel.result.observeAsState()
    val wlResult by viewModel.wl.observeAsState()
    val scope = rememberCoroutineScope()

    if (playerResult == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.purple_900)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        val tabs = listOf(
            stringResource(id = R.string.tab_text_1),
            stringResource(id = R.string.tab_text_2),
            stringResource(id = R.string.tab_text_3),
            stringResource(id = R.string.tab_text_4)
        )
        // Set beyondBoundsPageCount to keep all tabs in memory for smooth scrolling
        val pagerState = rememberPagerState(pageCount = { tabs.size })

        Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.purple_900))) {
            AccountHeader(
                player = playerResult!!,
                wl = wlResult
            )

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
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
                beyondViewportPageCount = 3 // Pre-loads all tabs
            ) { page ->
                when (page) {
                    0 -> MatchesScreen(viewModel, constViewModel, onMatchClick)
                    1 -> HeroesScreen(viewModel, constViewModel, onHeroClick)
                    2 -> PeersScreen(viewModel, onPeerClick)
                    3 -> TotalsScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun AccountHeader(
    player: PlayersResult,
    wl: WLResult?
) {
    val context = LocalContext.current
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
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Portrait Section: Avatar
                    AsyncImage(
                        model = player.profile?.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    // Info Section
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = player.profile?.name ?: "Unknown",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                        Text(
                            text = "ID: ${player.profile?.accountId}",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        if (player.leaderboardRank != null) {
                            Text(
                                text = "Leaderboard: #${player.leaderboardRank}",
                                color = Color(0xFFFFD700),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Rank Medal Section
                    val rankResId = remember(player.rankTier, player.leaderboardRank) {
                        if (player.rankTier == null) R.drawable.r00
                        else {
                            val name = if (player.leaderboardRank != null) {
                                when {
                                    try { player.leaderboardRank!!.toInt() <= 10 } catch(e: Exception) { false } -> "r80"
                                    try { player.leaderboardRank!!.toInt() <= 100 } catch(e: Exception) { false } -> "r81"
                                    else -> "r82"
                                }
                            } else "r${player.rankTier}"
                            val id = context.resources.getIdentifier(name, "drawable", context.packageName)
                            if (id != 0) id else R.drawable.r00
                        }
                    }
                    Image(
                        painter = painterResource(id = rankResId),
                        contentDescription = "Rank Medal",
                        modifier = Modifier.size(56.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Stats Section
                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WLStat(label = "WINS", value = wl?.win ?: 0, color = colorResource(id = R.color.green))
                        Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color.White.copy(alpha = 0.1f)))
                        WLStat(label = "LOSSES", value = wl?.lose ?: 0, color = colorResource(id = R.color.red))
                        Box(modifier = Modifier.height(20.dp).width(1.dp).background(Color.White.copy(alpha = 0.1f)))
                        
                        val total = (wl?.win ?: 0) + (wl?.lose ?: 0)
                        val winrate = if (total > 0) (wl!!.win!!.toDouble() / total * 100) else 0.0
                        WLStat(label = "WINRATE", value = String.format(Locale.getDefault(), "%.1f%%", winrate), color = Color.Yellow)
                    }
                }
            }
        }
    }
}

@Composable
fun WLStat(label: String, value: Any, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label, 
            color = Color.Gray, 
            fontSize = 10.sp, 
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value.toString(), 
            color = color, 
            fontWeight = FontWeight.ExtraBold, 
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
fun AccountHeaderPreview() {
    MaterialTheme {
        AccountHeader(
            player = PlayersResult(
                rankTier = 75,
                leaderboardRank = "123",
                profile = Profile(
                    accountId = 12345678,
                    name = "Dota Player",
                    avatar = ""
                )
            ),
            wl = WLResult(win = 150, lose = 100)
        )
    }
}
