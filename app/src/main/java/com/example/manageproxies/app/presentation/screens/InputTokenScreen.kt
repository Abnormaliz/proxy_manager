package com.example.manageproxies.app.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.vm.SharedViewModel


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun InputKeyScreen(navController: NavController, viewModel: SharedViewModel) {
    var apiKey by remember { mutableStateOf(TextFieldValue(viewModel.getToken().value.toString())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("Enter API Key") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.saveToken(Token(apiKey.text))
                viewModel.getServerApi()
                viewModel.getModemApi()
                navController.navigate("listOfModemsScreen")
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Accept")
        }
    }
}