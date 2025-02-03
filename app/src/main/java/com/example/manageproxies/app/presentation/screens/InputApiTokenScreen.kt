package com.example.manageproxies.app.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.InputApiTokenIntent
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.vm.InputApiTokenScreenViewModel

@Composable
fun InputApiTokenScreen(viewModel: InputApiTokenScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isServerAdded) {
        if (uiState.isServerAdded) {
            focusManager.clearFocus()
            Toast.makeText(context, "Сервер успешно добавлен", Toast.LENGTH_SHORT).show()
            viewModel.resetServerAddedFlag()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        InputField(
            value = uiState.nameTextField,
            onValueChange = { viewModel.handleIntent(InputApiTokenIntent.NameChanged(it)) },
            label = stringResource(R.string.api_token_name_hint),
            errorText = uiState.errors["name"]
        )
        InputField(
            value = uiState.tokenTextField,
            onValueChange = { viewModel.handleIntent(InputApiTokenIntent.TokenChanged(it)) },
            label = stringResource(R.string.api_token_value_hint),
            errorText = uiState.errors["token"]
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.handleIntent(InputApiTokenIntent.SaveApiToken) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = stringResource(R.string.api_token_add_button),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
        )
        {
            uiState.errors["requestError"]?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ShowApiTokens(uiState.apiTokensList)
    }
}

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String, errorText: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        errorText?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
        }
    }
}

@Composable
fun ShowApiTokens(tokens: List<ApiToken>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        items(tokens) { token ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        text = token.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = token.value.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}



