package com.example.dotametrics.presentation.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.domain.entity.remote.search.SearchResult
import com.example.dotametrics.util.Datetime
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState,
    onPlayerClick: (Long) -> Unit
) {
    val tabs = listOf(
        stringResource(id = R.string.tab_main_1),
        stringResource(id = R.string.tab_main_2)
    )
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()
    val error by viewModel.error.observeAsState()

    LaunchedEffect(error) {
        if (error == "rate_limit_exceeded") {
            snackbarHostState.showSnackbar("Limit reached. Please try again in a few seconds.")
            viewModel.clearError()
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(id = R.color.purple_700),
            colorResource(id = R.color.purple_900)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple_900))
    ) {
        // App Icon with purple_700 background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.purple_700))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(id = R.color.purple_700),
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
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { page ->
            Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
                when (page) {
                    0 -> SearchScreen(viewModel, onPlayerClick)
                    1 -> FavoritesScreen(viewModel, onPlayerClick)
                }
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel: MainViewModel, onPlayerClick: (Long) -> Unit) {
    var query by remember { mutableStateOf("") }
    val results by viewModel.results.observeAsState(emptyList())
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(results) {
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search players...", color = Color.Gray) },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    IconButton(onClick = {
                        if (query.isNotBlank()) {
                            viewModel.search(query)
                            isLoading = true
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (query.isNotBlank()) {
                    viewModel.search(query)
                    isLoading = true
                }
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.Black.copy(alpha = 0.2f)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results) { player ->
                SearchResultItem(player, onPlayerClick)
            }
        }
    }
}

@Composable
fun SearchResultItem(player: SearchResult, onPlayerClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerClick(player.accountId ?: 0L) },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = player.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = player.name ?: "Unknown", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "ID: ${player.accountId}", color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = "Last match: ${Datetime.formatDate(player.lastMatchTime)}",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(viewModel: MainViewModel, onPlayerClick: (Long) -> Unit) {
    val favorites by viewModel.players.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (favorites.isEmpty()) {
            Text(
                text = "No favorites yet",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favorites) { player ->
                    FavoriteItem(player, onPlayerClick)
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(player: PlayerDbModel, onPlayerClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayerClick(player.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = player.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = player.name ?: "Unknown", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "ID: ${player.id}", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}
