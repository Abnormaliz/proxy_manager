package com.example.manageproxies.app.presentation.navigation


sealed class Screen(val route: String, val title: String) {
    object Servers : Screen("Servers", "Servers")
    object Tokens : Screen("Tokens", "Tokens")
    data class Modems(val serverDomain: String) : Screen("Modems/{serverDomain}", "Modems") {
        fun createRoute(serverDomain: String) = "Modems/$serverDomain"
    }
}
