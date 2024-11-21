package com.example.manageproxies.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
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
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.navigation.Screen
import com.example.manageproxies.app.presentation.screens.InputKeyScreen
import com.example.manageproxies.app.presentation.screens.ModemsInfoScreen
import com.example.manageproxies.app.presentation.screens.ServerInfoScreen
import com.example.manageproxies.app.presentation.ui.theme.AppTheme
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
    val navController = rememberNavController()
    var selectedItemIndex by remember {
        mutableIntStateOf(0)
    }
    val screens = listOf(
        Screen(
            "Servers",
            "ListOfServersScreen",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Default.AccountCircle
        ),
        Screen(
            "Modems",
            "ListOfModemsScreen",
            selectedIcon = Icons.Filled.Menu,
            unselectedIcon = Icons.Sharp.Menu
        ),
        Screen(
            "Add",
            "AddServerScreen",
            selectedIcon = Icons.Filled.AddCircle,
            unselectedIcon = Icons.Default.AddCircle
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
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
            startDestination = "ListOfServersScreen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("AddServerScreen") {
                InputKeyScreen(sharedViewModel)
            }
            composable("ListOfServersScreen") {
                ServerInfoScreen(sharedViewModel)
            }
            composable("ListOfModemsScreen") {
                ModemsInfoScreen(sharedViewModel)
            }
        }
    }
}
