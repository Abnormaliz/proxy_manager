package com.example.manageproxies.app.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.vm.SharedViewModel
import com.example.manageproxies.data.remote.Modem
import com.example.manageproxies.data.remote.ServerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ListOfModemsScreen(viewModel : SharedViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var serverInfo by remember { mutableStateOf<List<ServerInfo>>(emptyList()) }
    var modem by remember { mutableStateOf<List<Modem>>(emptyList()) }

    coroutineScope.launch(Dispatchers.IO) {
//        serverInfo = viewModel.getServerInfoApi()
        try {
            modem = viewModel.getModemApi()
        } catch (e: Exception) {
            modem = modem
        }
    }

    Column {
//        LazyColumn {
//            item {
//                serverInfo.forEach {
//                    Text(it.toString())
//                }
//            }
//        }
        LazyColumn {
            item {
                modem.forEach {
                    Text(it.toString())
                }
            }
        }
    }
}