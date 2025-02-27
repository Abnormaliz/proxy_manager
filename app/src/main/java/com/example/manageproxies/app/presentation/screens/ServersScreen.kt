package com.example.manageproxies.app.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.vm.ServersScreenIntent
import com.example.manageproxies.app.presentation.vm.ServersScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun ServerInfoScreen(viewModel: ServersScreenViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)


    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        CustomToolBar(uiState.totalIncome, uiState.amountOfServers)
        Box(modifier = Modifier.fillMaxWidth()) {
            Column {
                SwipeRefresh(
                    state = swipeRefreshState, onRefresh = {
                        viewModel.handleIntent(
                            ServersScreenIntent.UpdateServersScreen
                        )
                    }, modifier = Modifier.fillMaxSize()
                ) {
                    ShowServers(uiState.serverList, navController)
                }

            }
        }
    }
}


@Composable
fun ShowServers(
    serverInfo: List<ServerInfoUi>?,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.wrapContentSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        serverInfo?.let { servers ->
            items(servers) { server ->
                ShowServerInfo(
                    server = server,
                    navController = navController
                )
            }

        }
    }
}

@Composable
fun ShowServerInfo(
    server: ServerInfoUi,
    navController: NavController
) {
    var areOrdersExpanded by remember { mutableStateOf(false) }
    var areModemsExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = { navController.navigate("ListOfModemsScreen") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                ServerInfoFormattedText(R.string.server_id, server.id)
                ServerInfoFormattedText(R.string.server_geo, server.geo)
                ServerInfoFormattedText(R.string.server_income, server.totalIncome.toString(), " ₽")
                ServerInfoFormattedText(
                    R.string.server_modems_selling,
                    server.sellingModems.toString(),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { areModemsExpanded = !areModemsExpanded }
                )
                AnimatedVisibility(visible = areModemsExpanded) {
                    Column {
                        ServerInfoFormattedText(
                            R.string.server_modems_activated,
                            server.activatedModems.toString()
                        )
                        ServerInfoFormattedText(
                            R.string.server_modems_total,
                            server.allModems.toString()
                        )
                    }
                }
                ServerInfoFormattedText(
                    R.string.server_orders_total,
                    server.allOrders.toString(),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { areOrdersExpanded = !areOrdersExpanded })

                AnimatedVisibility(visible = areOrdersExpanded) {
                    Column {
                        ServerInfoFormattedText(
                            R.string.server_orders_clients,
                            server.siteOrders.toString()
                        )
                        ServerInfoFormattedText(
                            R.string.server_orders_self,
                            server.selfOrders.toString()
                        )
                        ServerInfoFormattedText(
                            R.string.server_orders_test,
                            server.testOrders.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomToolBar(
    totalIncome: Int,
    amountOfServers: Int
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
                Text(text = stringResource(R.string.servers_total_income))
                CustomNumberDisplay(totalIncome, " ₽")

            }
            Column {
                Text(text = stringResource(R.string.servers_amount_of_servers))
                CustomNumberDisplay(amountOfServers)
            }
        }
    }

}

@Composable
fun CustomNumberDisplay(number: Int, args: String? = null) {
    val numberString = number.toString()

    val formattedText = buildAnnotatedString {
        val mainPartSize = 40.sp
        val smallPartSize = 25.sp

        if (number >= 1000) {
            val mainPart = numberString.dropLast(3)
            val smallPart = numberString.takeLast(3)


            withStyle(style = SpanStyle(fontSize = mainPartSize)) {
                append(mainPart)
            }
            append(".")
            withStyle(style = SpanStyle(fontSize = smallPartSize)) {
                append(smallPart)
            }
        } else {
            withStyle(style = SpanStyle(fontSize = mainPartSize)) {
                append(numberString.first())
            }
            withStyle(style = SpanStyle(fontSize = smallPartSize)) {
                append(numberString.drop(1))

            }
        }
        args?.let {
            withStyle(style = SpanStyle(fontSize = smallPartSize)) {
                append(it)
            }
        }

    }
    Text(text = formattedText)
}

@Composable
fun ServerInfoFormattedText(
    stringResource: Int,
    data: String,
    args: String? = null,
    textDecoration: TextDecoration? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Thin,
                    textDecoration = textDecoration ?: TextDecoration.None
                )
            ) {
                append(stringResource(stringResource))

            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(" ")
                append(data)
                args?.let { append(it) }

            }
        },
        modifier = modifier
    )
}


