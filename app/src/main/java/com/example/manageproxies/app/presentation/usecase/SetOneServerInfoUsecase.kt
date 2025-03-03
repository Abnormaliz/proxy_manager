package com.example.manageproxies.app.presentation.usecase

import android.util.Log
import com.example.manageproxies.app.presentation.models.ApiToken
import com.example.manageproxies.app.presentation.models.ServerInfoUi
import com.example.manageproxies.app.presentation.models.extractDigitsOnly
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SetOneServerInfoUsecase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend fun getServerInfo(apiTokenList: List<ApiToken>, serverDomain: String): ServerInfoUi? {

        val tokens = apiTokenList.map { it.value.toString() }

        return coroutineScope {
            tokens.map { token ->
                async {
                    val serverData = repository.getServerFromApi(token)
                    val modemData = repository.getModemsFromApi(token).map { it.toModemUi() }

                    val server = serverData.firstOrNull { it.server_domain == serverDomain }
                    if (server == null) {
                        return@async null
                    }

                    val relatedModems =
                        modemData.filter { it.domain.contains(server.server_domain) }

                    return@async ServerInfoUi(
                        token = token,
                        id = server.server_id,
                        geo = server.server_geo,
                        modemList = relatedModems,
                        domain = server.server_domain,
                        totalIncome = server.server_approximate_income.extractDigitsOnly().toInt(),
                        allModems = relatedModems.size,
                        sellingModems = relatedModems.count { it.isSelling },
                        activatedModems = relatedModems.count { it.isActivated },
                        allOrders = relatedModems.count { it.isOrdered },
                        siteOrders = relatedModems.count { it.isOnSiteOrdered },
                        selfOrders = relatedModems.count { it.isSelfOrdered },
                        testOrders = relatedModems.count { it.isTestOrdered }
                    )
                }
            }.awaitAll().filterNotNull().firstOrNull()
        }
    }
}

