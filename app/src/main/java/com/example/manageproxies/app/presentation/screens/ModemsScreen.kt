package com.example.manageproxies.app.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.vm.ModemsScreenIntent
import com.example.manageproxies.app.presentation.vm.ModemsScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun ModemsScreen(
    viewModel: ModemsScreenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(uiState.errors) {
        uiState.errors?.let { message ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message.values.joinToString("\n"))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            uiState.server?.allOrders?.let { allOrders ->
                uiState.server!!.sellingModems?.let { sellingModems ->
                    CustomToolBar(sellingModems, allOrders)
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    SwipeRefresh(
                        state = swipeRefreshState,
                        onRefresh = {
                            viewModel.handleIntent(ModemsScreenIntent.UpdateModemsScreen)
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ShowModems(uiState.server?.modemList)
                    }
                }
            }
        }
    }
}


@Composable
fun ShowModems(modems: List<ModemUi>?) {
    LazyColumn(
        modifier = Modifier
            .wrapContentSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Log.d("123", "$modems")
        modems?.let {
            items(it.size) { index ->
                ModemRow(modem = it[index])
            }
        }
    }
}


@Composable
fun ModemRow(modem: ModemUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (modem.isOrdered) {
                        Modifier.background(color = MaterialTheme.colorScheme.outline)
                    } else Modifier
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (modem.status == true)
                    Image(painterResource(R.drawable.status_on), contentDescription = "statusOn")
                else Image(painterResource(R.drawable.status_off), contentDescription = "statusOff")
                Text(text = modem.eid.toString())
                Text(text = modem.name)
                Text(text = modem.operator)
            }
        }
    }
}


@Composable
private fun CustomToolBar(
    areSelling: Int,
    allOrders: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = stringResource(R.string.modems_are_selling))
                CustomNumberDisplay(areSelling)

            }
            Column {
                Text(text = stringResource(R.string.modems_all_orders))
                CustomNumberDisplay(allOrders)
            }
        }
    }
}

@Composable
private fun CustomNumberDisplay(number: Int, args: String? = null) {
    val numberString = number.toString()

    val formattedText = buildAnnotatedString {
        val textSize = 40.sp

        withStyle(style = SpanStyle(fontSize = textSize)) {
            append(numberString)
        }
        args?.let {
            withStyle(style = SpanStyle(fontSize = textSize)) {
                append(it)
            }
        }

    }
    Text(text = formattedText)
}