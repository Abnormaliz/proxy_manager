package com.example.manageproxies.app.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.vm.SharedViewModel


@Composable
fun ListOfModemsScreen(viewModel : SharedViewModel) {
    var token by remember { mutableStateOf("no token here") }

    Column {
        Text(text = token)

        Button(
            onClick = {
                token = viewModel.getToken().value.toString()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Token")
        }
    }


}