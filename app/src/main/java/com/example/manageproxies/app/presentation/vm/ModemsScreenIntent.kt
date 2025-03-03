package com.example.manageproxies.app.presentation.vm

sealed class ModemsScreenIntent {
    object UpdateModemsScreen : ModemsScreenIntent()
    data class UpdateServerDomain(val serverDomain: String) : ModemsScreenIntent()
}
