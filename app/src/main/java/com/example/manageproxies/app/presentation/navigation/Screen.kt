package com.example.manageproxies.app.presentation.navigation


sealed class Screen(val route: String, val title: String) {
    data object Servers : Screen("Servers", "Servers")
    data object Tokens : Screen("Tokens", "Tokens")
    data object Modems : Screen("Modems/{serverDomain}", "Modems") {
        fun createRoute(serverDomain: String) = "Modems/$serverDomain"
    }
}
