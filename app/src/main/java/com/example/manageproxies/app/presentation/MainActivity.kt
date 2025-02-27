package com.example.manageproxies.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.navigation.Screen
import com.example.manageproxies.app.presentation.screens.InputApiTokenScreen
import com.example.manageproxies.app.presentation.screens.ModemsInfoScreen
import com.example.manageproxies.app.presentation.screens.ServerInfoScreen
import com.example.manageproxies.app.presentation.ui.theme.AppTheme
import com.example.manageproxies.app.presentation.vm.InputApiTokenScreenViewModel
import com.example.manageproxies.app.presentation.vm.ServersScreenViewModel
import com.example.manageproxies.app.presentation.vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.background))
                ) {
                    BottomNavigationBar()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    val sharedViewModel = hiltViewModel<SharedViewModel>()
    val serversScreenViewModel = hiltViewModel<ServersScreenViewModel>()
    val inputApiTokenScreenViewModel = hiltViewModel<InputApiTokenScreenViewModel>()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val screens = listOf(
        Screen(
            "Servers",
            "ServersScreen",
            selectedIcon = ImageVector.vectorResource(R.drawable.servers_bold_icon),
            unselectedIcon = ImageVector.vectorResource(R.drawable.servers_bold_icon)
        ),
        Screen(
            "Modems",
            "ListOfModemsScreen",
            selectedIcon = ImageVector.vectorResource(R.drawable.modems_bold_icon),
            unselectedIcon = ImageVector.vectorResource(R.drawable.modems_bold_icon)
        ),
        Screen(
            "Tokens",
            "InputApiTokenScreen",
            selectedIcon = ImageVector.vectorResource(R.drawable.tokens_bold_icon),
            unselectedIcon = ImageVector.vectorResource(R.drawable.tokens_bold_icon)
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
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
                                imageVector = if (currentRoute == screen.route) {
                                    screen.selectedIcon
                                } else screen.unselectedIcon, contentDescription = screen.title
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
            startDestination = "ServersScreen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("InputApiTokenScreen") {
                InputApiTokenScreen(inputApiTokenScreenViewModel)
            }
            composable("ServersScreen") {
                ServerInfoScreen(serversScreenViewModel, navController = navController)
            }
            composable("ListOfModemsScreen") {
                ModemsInfoScreen(sharedViewModel)
            }
        }
    }
}
