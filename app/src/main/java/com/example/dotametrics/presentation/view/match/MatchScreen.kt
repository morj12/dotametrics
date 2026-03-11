package com.example.dotametrics.presentation.view.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.matches.MatchDataResult
import com.example.dotametrics.domain.entity.remote.matches.Players
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.util.Datetime
import com.example.dotametrics.util.LobbyTypeMapper
import com.example.dotametrics.util.UrlConstants

private val TEAM_HEADER_HEIGHT = 44.dp
private val TEAM_SPACER_HEIGHT = 16.dp
private val TOTAL_TABLE_WIDTH = 1900.dp

@Composable
fun MatchScreen(
    viewModel: MatchViewModel,
    constViewModel: ConstViewModel,
    onPlayerClick: (Long) -> Unit
) {
    val matchResult by viewModel.result.observeAsState()
    val regions by constViewModel.constRegions.observeAsState(emptyMap())
    val items by constViewModel.constItems.observeAsState(emptyMap())
    val lobbies by constViewModel.constLobbyTypes.observeAsState(emptyList())
    val heroes by constViewModel.heroes.observeAsState(emptyList())
    val abilityIds by constViewModel.constAbilityIds.observeAsState(emptyMap())

    LaunchedEffect(regions, items, lobbies) {
        if (regions.isNotEmpty() && items.isNotEmpty() && lobbies.isNotEmpty()) {
            if (viewModel.result.value == null) viewModel.loadMatch()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.purple_900))) {
        if (matchResult == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            MatchHeader(matchResult!!, lobbies, regions)

            val horizontalScrollState = rememberScrollState()

            Box(modifier = Modifier.fillMaxSize().horizontalScroll(horizontalScrollState)) {
                LazyColumn(
                    modifier = Modifier.width(TOTAL_TABLE_WIDTH).fillMaxHeight(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        TableHeader()
                    }

                    item {
                        TeamHeader(name = stringResource(id = R.string.radiant), color = colorResource(id = R.color.green))
                    }
                    
                    val radiantPlayers = matchResult!!.players.filter { (it.playerSlot ?: 0) < 100 }.sortedBy { it.playerSlot }
                    items(radiantPlayers) { player ->
                        UnifiedPlayerRow(player, heroes, items, abilityIds, onPlayerClick)
                    }

                    item { Spacer(modifier = Modifier.height(TEAM_SPACER_HEIGHT)) }

                    item {
                        TeamHeader(name = stringResource(id = R.string.dire), color = colorResource(id = R.color.red))
                    }

                    val direPlayers = matchResult!!.players.filter { (it.playerSlot ?: 0) >= 100 }.sortedBy { it.playerSlot }
                    items(direPlayers) { player ->
                        UnifiedPlayerRow(player, heroes, items, abilityIds, onPlayerClick)
                    }
                    
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("PLAYER", Modifier.width(200.dp), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(17.dp))
        Text("KDA", Modifier.width(80.dp), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.width(17.dp))
        Text("ITEMS", Modifier.width(150.dp), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.width(17.dp))
        Text("STATISTICS", Modifier.width(450.dp), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.width(17.dp))
        Text("SKILLS (LEVEL 1-30)", Modifier.width(900.dp), color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@Composable
fun UnifiedPlayerRow(
    player: Players,
    heroes: List<com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult>,
    itemsMap: Map<String, com.example.dotametrics.domain.entity.remote.constants.items.ItemResult>,
    abilityIds: Map<String, String>,
    onPlayerClick: (Long) -> Unit
) {
    val hero = heroes.find { it.id == player.heroId }
    val isAnonymous = player.accountId == null
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (!isAnonymous) Modifier.clickable { onPlayerClick(player.accountId ?: 0L) } else Modifier),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Hero Info Section (200dp)
            Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    val heroName = hero?.name?.replace("npc_dota_hero_", "")
                    AsyncImage(
                        model = "${UrlConstants.HEROES_URL}/$heroName.png",
                        contentDescription = null,
                        modifier = Modifier.size(72.dp, 40.dp).clip(RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(22.dp).offset(x = 6.dp, y = 6.dp),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = player.level.toString(), color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = player.name ?: stringResource(id = R.string.anonymous), color = Color.White, fontSize = 13.sp, maxLines = 1, fontWeight = FontWeight.Bold)
                    if (player.rankTier != null) {
                        val rankResId = context.resources.getIdentifier("r${player.rankTier}", "drawable", context.packageName)
                        if (rankResId != 0) {
                            Image(painter = painterResource(id = rankResId), contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            VerticalDivider()

            // 2. KDA Section (80dp)
            Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "${player.kills ?: 0} / ${player.deaths ?: 0} / ${player.assists ?: 0}",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            VerticalDivider()

            // 3. Items Section (150dp)
            Row(modifier = Modifier.width(150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        ItemIcon(player.item0, itemsMap)
                        ItemIcon(player.item1, itemsMap)
                        ItemIcon(player.item2, itemsMap)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        ItemIcon(player.item3, itemsMap)
                        ItemIcon(player.item4, itemsMap)
                        ItemIcon(player.item5, itemsMap)
                    }
                }
                ItemIcon(player.itemNeutral, itemsMap, isNeutral = true)
            }

            VerticalDivider()

            // 4. Stats Section (450dp)
            Row(modifier = Modifier.width(450.dp), horizontalArrangement = Arrangement.SpaceAround) {
                StatColumn("GOLD", player.totalGold.toString(), Color.Yellow)
                StatColumn("LH/DN", "${player.lastHits}/${player.denies}")
                StatColumn("GPM", player.goldPerMin?.toInt()?.toString() ?: "0", Color.Yellow)
                StatColumn("XPM", player.xpPerMin.toString())
                StatColumn("HERO DMG", player.heroDamage.toString())
                StatColumn("TOW DMG", player.towerDamage.toString())
            }

            VerticalDivider()

            // 5. Skills Section (Place for 30 skills with dividers)
            Row(
                modifier = Modifier.width(900.dp).padding(start = 8.dp), 
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                player.abilityUpgradesArr.take(30).forEachIndexed { index, abilityId ->
                    val abilityName = abilityIds[abilityId.toString()]
                    if (abilityName != null) {
                        val type = when {
                            abilityName.contains("special_bonus") -> "talent"
                            abilityName.contains("attribute") -> "attributes"
                            else -> "ability"
                        }
                        AbilityIcon(abilityName, type, modifier = Modifier.size(24.dp))
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                    
                    // Dividers after 6, 12, 18, 25 levels (indices 5, 11, 17, 24)
                    if (index == 5 || index == 11 || index == 17 || index == 24) {
                        VerticalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalDivider() {
    Box(modifier = Modifier.padding(horizontal = 8.dp).width(1.dp).height(40.dp).background(Color.White.copy(alpha = 0.1f)))
}

@Composable
fun StatColumn(label: String, value: String, color: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
        Text(text = label, color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MatchHeader(match: MatchDataResult, lobbies: List<com.example.dotametrics.domain.entity.remote.constants.lobbytypes.LobbyTypeResult>, regions: Map<Int, String>) {
    val context = LocalContext.current
    val headerBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.purple_700),
            colorResource(id = R.color.purple_900)
        )
    )

    Box(modifier = Modifier.fillMaxWidth().background(headerBrush)) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.radiantScore.toString(),
                        color = colorResource(id = R.color.green),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (match.radiantWin == true) stringResource(id = R.string.radiant_won) else stringResource(id = R.string.dire_won),
                            color = if (match.radiantWin == true) colorResource(id = R.color.green) else colorResource(id = R.color.red),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = Datetime.getStringTime(match.duration ?: 0, context),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = match.direScore.toString(),
                        color = colorResource(id = R.color.red),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    val lobbyName = lobbies.find { it.id == match.lobbyType }?.name
                    val lobbyText = if (lobbyName != null) {
                        val resId = LobbyTypeMapper().getLobbyResource(lobbyName, context)
                        if (resId != 0) stringResource(resId) else lobbyName
                    } else "Unknown"

                    Text(text = lobbyText, color = Color.Gray, fontSize = 12.sp)
                    Text(text = regions[match.region] ?: "Unknown", color = Color.Gray, fontSize = 12.sp)
                    Text(text = Datetime.formatDate(match.startTime ?: 0), color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TeamHeader(name: String, color: Color) {
    Box(modifier = Modifier.fillMaxWidth().height(TEAM_HEADER_HEIGHT), contentAlignment = Alignment.CenterStart) {
        Text(
            text = name,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ItemIcon(itemId: Int?, itemsMap: Map<String, com.example.dotametrics.domain.entity.remote.constants.items.ItemResult>, isNeutral: Boolean = false) {
    val itemEntry = itemsMap.entries.find { it.value.id == itemId }
    val shape = if (isNeutral) CircleShape else RoundedCornerShape(1.dp)
    val sizeModifier = if (isNeutral) Modifier.size(38.dp) else Modifier.size(30.dp, 22.dp)
    
    Box(
        modifier = sizeModifier
            .clip(shape)
            .background(Color.Black.copy(alpha = 0.3f))
            .then(if (isNeutral) Modifier.border(0.5.dp, Color.Gray.copy(alpha = 0.5f), CircleShape) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        if (itemEntry != null) {
            AsyncImage(
                model = "${UrlConstants.ITEMS_URL}/${itemEntry.key}.png",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun AbilityIcon(name: String, type: String, modifier: Modifier = Modifier) {
    val iconModifier = modifier.aspectRatio(1f)
    when (type) {
        "talent" -> {
            Image(
                painter = painterResource(id = R.drawable.talent),
                contentDescription = null,
                modifier = iconModifier.clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop
            )
        }
        "attributes" -> {
            Image(
                painter = painterResource(id = R.drawable.attr),
                contentDescription = null,
                modifier = iconModifier.clip(RoundedCornerShape(2.dp)),
                contentScale = ContentScale.Crop
            )
        }
        else -> {
            AsyncImage(
                model = "${UrlConstants.ABILITIES_URL}/$name.png",
                contentDescription = null,
                modifier = iconModifier.clip(RoundedCornerShape(1.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(widthDp = 1000)
@Composable
fun UnifiedPlayerRowPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(colorResource(id = R.color.purple_900))) {
            UnifiedPlayerRow(
                player = Players(
                    heroId = 1,
                    accountId = 123456,
                    name = "Player Name",
                    level = 25,
                    kills = 15,
                    deaths = 5,
                    assists = 20,
                    totalGold = 25000,
                    lastHits = 250,
                    denies = 10,
                    goldPerMin = 650.0,
                    xpPerMin = 700,
                    heroDamage = 35000,
                    towerDamage = 5000,
                    abilityUpgradesArr = arrayListOf(1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 5001, 2, 3, 5002, 5003)
                ),
                heroes = emptyList(),
                itemsMap = emptyMap(),
                abilityIds = emptyMap(),
                onPlayerClick = {}
            )
        }
    }
}

@Preview
@Composable
fun MatchHeaderPreview() {
    MaterialTheme {
        MatchHeader(
            match = MatchDataResult(
                radiantScore = 45,
                direScore = 32,
                radiantWin = true,
                duration = 2400,
                lobbyType = 7,
                region = 3,
                startTime = 1672531200
            ),
            lobbies = emptyList(),
            regions = mapOf(3 to "Europe")
        )
    }
}
