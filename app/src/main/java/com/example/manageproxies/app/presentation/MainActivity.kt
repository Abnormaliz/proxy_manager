package com.example.manageproxies.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manageproxies.app.presentation.screens.InputKeyScreen
import com.example.manageproxies.app.presentation.screens.ServerInfoScreen
import com.example.manageproxies.app.presentation.ui.theme.ManageProxiesTheme
import com.example.manageproxies.app.presentation.vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManageProxiesTheme {
                val sharedViewModel = hiltViewModel<SharedViewModel>()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "inputKey"
                ) {
                    composable("inputKey") {
                        InputKeyScreen(navController = navController, sharedViewModel)
                    }
                    composable("listOfModemsScreen") {
                        ServerInfoScreen(sharedViewModel)
                    }
                }
            }
        }
    }
}