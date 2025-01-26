package com.example.manageproxies.app.presentation.screens

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
import com.example.manageproxies.app.presentation.models.InputApiTokenIntent
import com.example.manageproxies.app.presentation.models.InputApiTokenState
import com.example.manageproxies.app.presentation.vm.InputApiTokenScreenViewModel

@Composable
fun InputApiTokenScreen(viewModel: InputApiTokenScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    if (uiState is InputApiTokenState.Input) {
        val inputState = uiState as InputApiTokenState.Input
        inputState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.handleIntent(InputApiTokenIntent.MessageShown)
        }
    }

    when (uiState) {
        is InputApiTokenState.Input -> {
            val state = uiState as InputApiTokenState.Input
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { viewModel.handleIntent(InputApiTokenIntent.NameChanged(it)) },
                    label = { Text(stringResource(R.string.api_token_name_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                state.errors["name"]?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                TextField(
                    value = state.token,
                    onValueChange = { viewModel.handleIntent(InputApiTokenIntent.TokenChanged(it)) },
                    label = { Text(stringResource(R.string.api_token_value_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                state.errors["token"]?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.handleIntent(InputApiTokenIntent.SaveApiToken) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(R.string.api_token_add_button))
                }
            }
        }

        is InputApiTokenState.Success -> {
            val message = (uiState as InputApiTokenState.Success).message
            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()

        }
        is InputApiTokenState.Error -> {
            val errors = (uiState as InputApiTokenState.Error).errors
            errors["general"]?.let { errorMessage ->
                Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        else -> {}
    }
}