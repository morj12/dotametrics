package com.example.dotametrics.presentation.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.local.PlayerDbModel
import com.example.dotametrics.domain.entity.remote.constants.heroes.HeroResult
import com.example.dotametrics.domain.entity.remote.teams.TeamsResult
import com.example.dotametrics.presentation.view.ConstViewModel
import com.example.dotametrics.presentation.view.account.AccountScreen
import com.example.dotametrics.presentation.view.account.AccountViewModel
import com.example.dotametrics.presentation.view.hero.HeroScreen
import com.example.dotametrics.presentation.view.hero.HeroViewModel
import com.example.dotametrics.presentation.view.herosearch.HeroSearchScreen
import com.example.dotametrics.presentation.view.herosearch.HeroSearchViewModel
import com.example.dotametrics.presentation.view.info.DotaMetricsTheme
import com.example.dotametrics.presentation.view.info.InfoScreen
import com.example.dotametrics.presentation.view.match.MatchScreen
import com.example.dotametrics.presentation.view.match.MatchViewModel
import com.example.dotametrics.presentation.view.team.TeamScreen
import com.example.dotametrics.presentation.view.team.TeamViewModel
import com.example.dotametrics.presentation.view.teamsearch.TeamSearchScreen
import com.example.dotametrics.presentation.view.teamsearch.TeamSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DotaMetricsTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                
                val purple700 = colorResource(id = R.color.purple_700)
                val purple900 = colorResource(id = R.color.purple_900)

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Manage System Bar Colors
                LaunchedEffect(drawerState.isOpen) {
                    window.navigationBarColor = purple900.toArgb()
                    window.statusBarColor = purple700.toArgb()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = purple900
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerContainerColor = purple900,
                                windowInsets = WindowInsets(0)
                            ) {
                                DrawerHeader()
                                Spacer(Modifier.height(12.dp))
                                
                                val navigationItems = listOf(
                                    NavigationItem("Players", "main", R.drawable.ic_person),
                                    NavigationItem("Heroes", "heroes", R.drawable.ic_hero),
                                    NavigationItem("Teams", "teams", R.drawable.ic_team),
                                    NavigationItem("About", "about", R.drawable.ic_info)
                                )

                                navigationItems.forEach { item ->
                                    NavigationDrawerItem(
                                        label = { Text(item.label, color = Color.White) },
                                        selected = false,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = false
                                                }
                                                launchSingleTop = true
                                                restoreState = false
                                            }
                                        },
                                        icon = { Icon(painterResource(id = item.iconRes), contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp)) },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                        colors = NavigationDrawerItemDefaults.colors(
                                            unselectedContainerColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        val title = when {
                                            currentRoute == "main" -> "Players"
                                            currentRoute == "heroes" -> "Heroes"
                                            currentRoute == "teams" -> "Teams"
                                            currentRoute == "about" -> "About"
                                            currentRoute?.startsWith("account/") == true -> {
                                                val accountEntry = remember(navBackStackEntry) {
                                                    navController.getBackStackEntry("account/{accountId}")
                                                }
                                                val accountViewModel: AccountViewModel = hiltViewModel(accountEntry)
                                                val playerResult by accountViewModel.result.observeAsState()
                                                playerResult?.profile?.name ?: "Account"
                                            }
                                            currentRoute == "hero_detail" -> {
                                                val hero = navController.previousBackStackEntry?.savedStateHandle?.get<HeroResult>("hero")
                                                hero?.localizedName ?: "Hero"
                                            }
                                            currentRoute == "team_detail" -> {
                                                val team = navController.previousBackStackEntry?.savedStateHandle?.get<TeamsResult>("team")
                                                team?.name ?: "Team"
                                            }
                                            currentRoute?.startsWith("match/") == true -> {
                                                val matchId = navBackStackEntry?.arguments?.getString("matchId")
                                                "Match #$matchId"
                                            }
                                            else -> "Dota Metrics"
                                        }
                                        Text(
                                            text = title,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu",
                                                tint = Color.White
                                            )
                                        }
                                    },
                                    actions = {
                                        if (currentRoute?.startsWith("account/") == true) {
                                            val accountEntry = remember(navBackStackEntry) {
                                                navController.getBackStackEntry("account/{accountId}")
                                            }
                                            val accountViewModel: AccountViewModel = hiltViewModel(accountEntry)
                                            val isFav by accountViewModel.isFav.observeAsState(false)
                                            val playerResult by accountViewModel.result.observeAsState()
                                            
                                            IconButton(onClick = {
                                                playerResult?.profile?.let { profile ->
                                                    val pid = profile.accountId?.toLong() ?: 0L
                                                    if (isFav) {
                                                        accountViewModel.deletePlayer(pid)
                                                    } else {
                                                        accountViewModel.insertPlayer(
                                                            PlayerDbModel(
                                                                id = pid,
                                                                name = profile.name,
                                                                avatar = profile.avatar
                                                            ),
                                                            observe = true
                                                        )
                                                    }
                                                }
                                            }) {
                                                Icon(
                                                    painter = painterResource(id = if (isFav) R.drawable.ic_fav else R.drawable.ic_fav_border),
                                                    contentDescription = "Favorite",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = purple700
                                    )
                                )
                            },
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            containerColor = Color.Transparent,
                            modifier = Modifier.navigationBarsPadding()
                        ) { padding ->
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = "main"
                                ) {
                                    composable("main") {
                                        val mainViewModel: MainViewModel = hiltViewModel()
                                        MainScreen(
                                            viewModel = mainViewModel, 
                                            snackbarHostState = snackbarHostState,
                                            onPlayerClick = { pid ->
                                                navController.navigate("account/$pid")
                                            }
                                        )
                                    }
                                    composable("heroes") {
                                        val heroSearchViewModel: HeroSearchViewModel = hiltViewModel()
                                        val constViewModel: ConstViewModel = hiltViewModel()
                                        
                                        LaunchedEffect(Unit) {
                                            constViewModel.loadHeroes()
                                        }

                                        HeroSearchScreen(
                                            viewModel = heroSearchViewModel,
                                            constViewModel = constViewModel,
                                            onHeroClick = { hero ->
                                                navController.currentBackStackEntry?.savedStateHandle?.set("hero", hero)
                                                navController.navigate("hero_detail")
                                            }
                                        )
                                    }
                                    composable("hero_detail") {
                                        val hero = navController.previousBackStackEntry?.savedStateHandle?.get<HeroResult>("hero")
                                        if (hero != null) {
                                            val heroViewModel: HeroViewModel = hiltViewModel()
                                            val constViewModel: ConstViewModel = hiltViewModel()
                                            HeroScreen(heroState = hero, viewModel = heroViewModel, constViewModel = constViewModel)
                                        }
                                    }
                                    composable("teams") {
                                        val teamSearchViewModel: TeamSearchViewModel = hiltViewModel()
                                        
                                        LaunchedEffect(Unit) {
                                            teamSearchViewModel.loadTeams()
                                        }
                                        
                                        TeamSearchScreen(
                                            viewModel = teamSearchViewModel,
                                            onTeamClick = { team ->
                                                navController.currentBackStackEntry?.savedStateHandle?.set("team", team)
                                                navController.navigate("team_detail")
                                            }
                                        )
                                    }
                                    composable("team_detail") {
                                        val team = navController.previousBackStackEntry?.savedStateHandle?.get<TeamsResult>("team")
                                        if (team != null) {
                                            val teamViewModel: TeamViewModel = hiltViewModel()
                                            val constViewModel: ConstViewModel = hiltViewModel()
                                            
                                            LaunchedEffect(team) {
                                                teamViewModel.setTeam(team)
                                                teamViewModel.loadPlayers()
                                                teamViewModel.loadMatches()
                                                teamViewModel.loadHeroes()
                                                constViewModel.loadHeroes()
                                            }
                                            
                                            TeamScreen(
                                                viewModel = teamViewModel,
                                                constViewModel = constViewModel,
                                                onPlayerClick = { player ->
                                                    navController.navigate("account/${player.accountId}")
                                                },
                                                onMatchClick = { match ->
                                                    navController.navigate("match/${match.matchId}")
                                                },
                                                onHeroClick = { hero ->
                                                    navController.currentBackStackEntry?.savedStateHandle?.set("hero", hero)
                                                    navController.navigate("hero_detail")
                                                }
                                            )
                                        }
                                    }
                                    composable("about") {
                                        InfoScreen()
                                    }
                                    composable(
                                        "account/{accountId}",
                                        arguments = listOf(navArgument("accountId") { type = NavType.LongType })
                                    ) { backStackEntry ->
                                        val accountId = backStackEntry.arguments?.getLong("accountId") ?: 0L
                                        val accountViewModel: AccountViewModel = hiltViewModel(backStackEntry)
                                        val constViewModel: ConstViewModel = hiltViewModel()
                                        
                                        LaunchedEffect(accountId) {
                                            accountViewModel.userId = accountId.toString()
                                            accountViewModel.loadUser()
                                            accountViewModel.loadMatches()
                                            accountViewModel.loadPlayerHeroesResults()
                                            accountViewModel.loadPeers()
                                            accountViewModel.loadTotals()
                                            accountViewModel.checkFavorite(accountId)
                                        }
                                        
                                        AccountScreen(
                                            viewModel = accountViewModel,
                                            constViewModel = constViewModel,
                                            onHeroClick = { hero ->
                                                navController.currentBackStackEntry?.savedStateHandle?.set("hero", hero)
                                                navController.navigate("hero_detail")
                                            },
                                            onMatchClick = { matchId ->
                                                navController.navigate("match/$matchId")
                                            },
                                            onPeerClick = { pid -> navController.navigate("account/$pid") }
                                        )
                                    }
                                    composable(
                                        "match/{matchId}",
                                        arguments = listOf(navArgument("matchId") { type = NavType.StringType })
                                    ) { backStackEntry ->
                                        val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
                                        val matchViewModel: MatchViewModel = hiltViewModel()
                                        val constViewModel: ConstViewModel = hiltViewModel()
                                        
                                        LaunchedEffect(matchId) {
                                            matchViewModel.matchId = matchId
                                            constViewModel.loadRegions()
                                            constViewModel.loadItems()
                                            constViewModel.loadLobbyTypes()
                                            constViewModel.loadHeroes()
                                            constViewModel.loadAbilityIds()
                                            matchViewModel.loadMatch()
                                        }
                                        
                                        MatchScreen(
                                            viewModel = matchViewModel,
                                            constViewModel = constViewModel,
                                            onPlayerClick = { pid ->
                                                navController.navigate("account/$pid")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(val label: String, val route: String, val iconRes: Int)

@Composable
fun DrawerHeader() {
    val purple700 = colorResource(id = R.color.purple_700)
    val purple900 = colorResource(id = R.color.purple_900)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(purple700, purple900)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Dota Metrics",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
