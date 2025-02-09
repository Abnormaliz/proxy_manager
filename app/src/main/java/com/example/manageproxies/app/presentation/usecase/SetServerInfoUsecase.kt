package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.extractDigitsOnly
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SetServerInfoUsecase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend fun getServerInfo(apiTokenList: List<ApiToken>): List<ServerInfoUi> {

        val tokens = apiTokenList.map { it.value.toString() }

        return coroutineScope {
            tokens.map { token ->
                async {
                    val deferredServerData =
                        async { repository.getServerFromApi(token) }
                    val deferredModemData =
                        async { repository.getModemsFromApi(token) }

                    val serverData = deferredServerData.await()
                    val modemData = deferredModemData.await().map { it.toModemUi() }

                    serverData.map { server ->

                        val relatedModems = modemData.filter { modem ->
                            modem.domain.contains(server.server_domain)
                        }
                        ServerInfoUi(id = server.server_id,
                            geo = server.server_geo,
                            totalIncome = server.server_approximate_income.extractDigitsOnly().toInt(),
                            allModems = relatedModems.size,
                            allOrders = relatedModems.count { it.isOrdered },
                            siteOrders = relatedModems.count { it.isOnSiteOrdered },
                            selfOrders = relatedModems.count { it.isSelfOrdered },
                            testOrders = relatedModems.count { it.isTestOrdered })
                    }
                }
            }
        }.awaitAll().flatten()
    }
}