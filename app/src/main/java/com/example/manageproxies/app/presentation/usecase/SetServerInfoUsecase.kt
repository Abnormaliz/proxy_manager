package com.example.manageproxies.app.presentation.usecase

import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.extractDigitsOnly
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SetServerInfoUsecase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend fun getServerInfo(apiTokenValue: String): List<ServerInfoUi> =

        coroutineScope {
            val deferredServerData =
                async { repository.getServerFromApi(apiTokenValue) }
            val deferredModemData =
                async { repository.getModemsFromApi(apiTokenValue) }

            val serverData = deferredServerData.await()
            val modemData = deferredModemData.await().map { it.toModemUi() }

            serverData.map { server ->
                ServerInfoUi(id = server.server_id,
                    geo = server.server_geo,
                    totalIncome = server.server_approximate_income.extractDigitsOnly().toInt(),
                    allModems = modemData.size,
                    allOrders = modemData.count { it.isOrdered },
                    siteOrders = modemData.count { it.isOnSiteOrdered },
                    selfOrders = modemData.count { it.isSelfOrdered },
                    testOrders = modemData.count { it.isTestOrdered })
            }
        }

}