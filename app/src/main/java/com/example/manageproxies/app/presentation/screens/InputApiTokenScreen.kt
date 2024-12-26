package com.example.manageproxies.app.presentation.screens

import android.health.connect.datatypes.units.Length
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.vm.InputApiTokenScreenViewModel

@Composable
fun InputApiTokenScreen(viewModel: InputApiTokenScreenViewModel) {

    val apiTokenName by viewModel.apiTokenName.collectAsState()
    val apiTokenValue by viewModel.apiTokenValue.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = apiTokenName,
            onValueChange = { viewModel.onNameChanged(it) },
            label = { Text(stringResource(R.string.api_token_name_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        TextField(
            value = apiTokenValue,
            onValueChange = { viewModel.onValueChanged(it) },
            label = { Text(stringResource(R.string.api_token_value_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.checkAndSaveApiToken(
                    onSuccess = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.api_token_add_button))
        }
    }
}