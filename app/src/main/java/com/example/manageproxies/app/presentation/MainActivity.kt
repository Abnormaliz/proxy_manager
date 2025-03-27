package com.example.manageproxies.app.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.navigation.Screen
import com.example.manageproxies.app.presentation.screens.ModemsScreen
import com.example.manageproxies.app.presentation.screens.ServersScreen
import com.example.manageproxies.app.presentation.screens.TokensScreen
import com.example.manageproxies.app.presentation.ui.theme.AppTheme
import com.example.manageproxies.app.presentation.usecase.ModemManager
import com.example.manageproxies.app.presentation.vm.ModemsScreenIntent
import com.example.manageproxies.app.presentation.vm.ModemsScreenViewModel
import com.example.manageproxies.app.presentation.vm.ServersScreenViewModel
import com.example.manageproxies.app.presentation.vm.TokensScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var modemManager: ModemManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.background))
                ) {
                    BottomNavigationBar(modemManager)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(modemManager: ModemManager) {
    val serversScreenViewModel = hiltViewModel<ServersScreenViewModel>()
    val modemsScreenViewModel = hiltViewModel<ModemsScreenViewModel>()
    val tokensScreenViewModel = hiltViewModel<TokensScreenViewModel>()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedServerDomain: String by remember { mutableStateOf("") }

    val screens = listOf(
        Screen.Servers to R.drawable.servers_bold_icon,
        Screen.Modems to R.drawable.modems_bold_icon,
        Screen.Tokens to R.drawable.tokens_bold_icon

    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { (screen, icon) ->
                    NavigationBarItem(

                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route)
                                navController.navigate(screen.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(icon),
                                contentDescription = screen.route
                            )
                        },
                        label = {
                            Text(text = screen.title)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Servers.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                route = Screen.Tokens.route
            ) {
                TokensScreen(tokensScreenViewModel)
            }
            composable(route = Screen.Servers.route) {
                ServersScreen(modemManager,
                    serversScreenViewModel,
                    onNavigateToModemsList = { serverDomain ->
                        modemsScreenViewModel.handleIntent(ModemsScreenIntent.UpdateServerDomain(serverDomain))
                        selectedServerDomain = serverDomain
                        navController.navigate(
                            Screen.Modems.createRoute(serverDomain)
                        )
                    })
            }
            composable(
                route = Screen.Modems.route,
                arguments = listOf(
                    navArgument("serverDomain") {
                        type = NavType.StringType
                        nullable = true
                    } // добавить сингл
                )
            ) { backStackEntry ->
                ModemsScreen(modemsScreenViewModel)
            }
        }
    }
}
