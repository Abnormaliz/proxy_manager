package com.example.manageproxies.app.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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


@Composable
fun ListOfModemsScreen(viewModel : SharedViewModel) {
    val serverInfo by viewModel.serverInfo.observeAsState()
    val modems by viewModel.modems.observeAsState()


    Column {
//        LazyColumn {
//            item {
//                serverInfo.forEach {
//                    Text(it.toString())
//                }
//            }
//        }
        Button(onClick = {
            viewModel.getModemApi()
        }) {
            Text("Get Modems")
        }
        LazyColumn {
            item {
                modems?.forEach {
                    Text(it.toString())
                }
            }
        }
    }
}