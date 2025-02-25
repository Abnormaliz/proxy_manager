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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.vm.ServersScreenIntent
import com.example.manageproxies.app.presentation.vm.ServersScreenViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun ServerInfoScreen(viewModel: ServersScreenViewModel) {
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
                    ShowServers(uiState.serverList)
                }

            }
        }
    }
}


@Composable
fun ShowServers(
    serverInfo: List<ServerInfoUi>?
) {
    LazyColumn(
        modifier = Modifier.wrapContentSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        serverInfo?.let { servers ->
            items(servers) { server ->
                ServerRow(
                    server = server
                )
            }

        }
    }
}

@Composable
fun ServerRow(
    server: ServerInfoUi
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                        append(stringResource(R.string.server_id, ""))
                    }

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(server.id)
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                        append(stringResource(R.string.server_geo, ""))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(server.geo)
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                        append(stringResource(R.string.server_income, ""))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(server.totalIncome.toString())
                        append(" ₽")
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                        append(stringResource(R.string.server_modems_total, ""))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(server.allModems.toString())
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                        append(stringResource(R.string.server_orders_total, ""))
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(server.allOrders.toString())
                    }
                }, modifier = Modifier.clickable { isExpanded = !isExpanded })

                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                                append(stringResource(R.string.server_orders_clients, ""))
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(server.siteOrders.toString())
                            }
                        })
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                                append(stringResource(R.string.server_orders_self, ""))
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(server.selfOrders.toString())
                            }
                        })
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Thin)) {
                                append(stringResource(R.string.server_orders_test, ""))
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(server.testOrders.toString())
                            }
                        })
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
            .padding(25.dp),
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
                CustomIncomeDisplay(totalIncome)

            }
            Column {
                Text(text = stringResource(R.string.servers_amount_of_servers))
                CustomServersDisplay(amountOfServers)
            }
        }
    }

}

@Composable
fun CustomIncomeDisplay(number: Int) {
    val numberString = number.toString()

    val formattedText = buildAnnotatedString {
        if (number >= 1000) {
            val mainPart = numberString.dropLast(3)
            val smallPart = numberString.takeLast(3)

            withStyle(style = SpanStyle(fontSize = 40.sp)) {
                append(mainPart)
            }
            append(".")
            withStyle(style = SpanStyle(fontSize = 25.sp)) {
                append(smallPart)
                append(" ₽")
            }
        } else {
            withStyle(style = SpanStyle(fontSize = 40.sp)) {
                append(numberString.first())
            }
            withStyle(style = SpanStyle(fontSize = 25.sp)) {
                append(numberString.drop(1))
                append(" ₽")
            }
        }
    }
    Text(text = formattedText)
}

@Composable
fun CustomServersDisplay(number: Int) {
    val numberString = number.toString()

    val formattedText = buildAnnotatedString {
        if (number >= 1000) {
            val mainPart = numberString.dropLast(3)
            val smallPart = numberString.takeLast(3)

            withStyle(style = SpanStyle(fontSize = 40.sp)) {
                append(mainPart)
            }
            append(".")
            withStyle(style = SpanStyle(fontSize = 30.sp)) {
                append(smallPart)
            }
        } else {
            withStyle(style = SpanStyle(fontSize = 40.sp)) {
                append(numberString.first())
            }
            withStyle(style = SpanStyle(fontSize = 30.sp)) {
                append(numberString.drop(1))
            }
        }
    }
    Text(text = formattedText)
}



