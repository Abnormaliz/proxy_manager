package com.example.manageproxies.app.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class Screen(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    )

