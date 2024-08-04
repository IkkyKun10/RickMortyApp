package com.riezki.rickmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riezki.network.KtorClient
import com.riezki.rickmortyapp.presenter.screens.CharacterDetailsScreen
import com.riezki.rickmortyapp.presenter.screens.CharacterEpisodeScreen
import com.riezki.rickmortyapp.presenter.screens.HomeScreens
import com.riezki.rickmortyapp.ui.theme.RickAction
import com.riezki.rickmortyapp.ui.theme.RickMortyAppTheme
import com.riezki.rickmortyapp.ui.theme.RickPrimary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                NavDestination.Home,
                NavDestination.Episodes,
                NavDestination.Search
            )
            var selectedIndex by remember { mutableIntStateOf(0) }

            RickMortyAppTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = RickPrimary
                        ) {
                            items.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    selected = index == selectedIndex,
                                    onClick = {
                                        selectedIndex = index
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(imageVector = screen.icon, contentDescription = null)
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RickAction,
                                        selectedTextColor = RickAction,
                                        indicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    },
                    ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        ktorClient = ktorClient,
                        innerPaddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    ktorClient: KtorClient,
    innerPaddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.Home.route,
        modifier = Modifier
            .background(color = RickPrimary)
            .padding(innerPaddingValues)
    ) {
        composable(route = NavDestination.Home.route) {
            HomeScreens { characterId ->
                navController.navigate("character_detail/$characterId")
            }
        }
        composable(
            route = "character_details/{characterId}",
            arguments = listOf(navArgument("characterId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
            CharacterDetailsScreen(
                characterId = characterId,
                onEpisodeClicked = {
                    navController.navigate("character_episodes/$it")
                },
                onBackClicked = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = "character_episodes/{characterId}",
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                }
            ),
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: -1
            CharacterEpisodeScreen(characterId = characterId, ktorClient = ktorClient) {
                navController.navigateUp()
            }
        }
        composable(
            route = NavDestination.Episodes.route
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Episodes", fontSize = 62.sp, color = Color.White)
            }
        }
        composable(
            route = NavDestination.Search.route
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Search", fontSize = 62.sp, color = Color.White)
            }
        }
    }
}

sealed class NavDestination(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object Home : NavDestination(title = "Home", route = "home_screen", icon = Icons.Filled.Home)
    data object Episodes : NavDestination(title = "Episodes", route = "episodes", icon = Icons.Filled.PlayArrow)
    data object Search : NavDestination(title = "Search", route = "search", icon = Icons.Filled.Search)
}
