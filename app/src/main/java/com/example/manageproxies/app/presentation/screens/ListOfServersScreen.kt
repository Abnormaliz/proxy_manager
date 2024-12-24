package com.example.manageproxies.app.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manageproxies.R
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.vm.SharedViewModel


@Composable
fun ServerInfoScreen(viewModel: SharedViewModel) {
    val server by viewModel.serverInfo.observeAsState()


    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            ShowServers(server)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(color = colorResource(R.color.block))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = buildAnnotatedString {
                append(stringResource(R.string.server_id, ""))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(server.id)
                }
            })
            Text(text = buildAnnotatedString {
                append(stringResource(R.string.server_geo, ""))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(server.geo)
                }
            })
            Text(text = buildAnnotatedString {
                append(stringResource(R.string.server_income, ""))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(server.totalIncome.toString())
                }
            })
            Text(text = buildAnnotatedString {
                append(stringResource(R.string.server_modems_total, ""))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(server.allModems.toString())
                }
            })
            Text(text = buildAnnotatedString {
                append(stringResource(R.string.server_orders_total, ""))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(server.allOrders.toString())
                }
            }, modifier = Modifier.clickable { isExpanded = !isExpanded })
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Text(text = buildAnnotatedString {
                        append(stringResource(R.string.server_orders_clients, ""))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(server.siteOrders.toString())
                        }
                    })
                    Text(text = buildAnnotatedString {
                        append(stringResource(R.string.server_orders_self, ""))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(server.selfOrders.toString())
                        }
                    })
                    Text(text = buildAnnotatedString {
                        append(stringResource(R.string.server_orders_test, ""))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(server.testOrders.toString())
                        }
                    })
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ModemRowPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(painterResource(R.drawable.status_off), contentDescription = "statusOn")
            Text(text = "123123")
            Text(text = "1")
            Text(text = "Viettel")
        }
    }
}

