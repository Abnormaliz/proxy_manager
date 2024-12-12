package com.example.manageproxies.app.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manageproxies.app.presentation.models.DailyStatistic
import com.example.manageproxies.app.presentation.models.ModemUi
import com.example.manageproxies.app.presentation.models.ServerUi
import com.example.manageproxies.app.presentation.models.Token
import com.example.manageproxies.app.presentation.models.toModemIpUi
import com.example.manageproxies.app.presentation.models.toModemUi
import com.example.manageproxies.app.presentation.models.toServerUi
import com.example.manageproxies.app.presentation.usecase.GetDailyStatisticFromDatabase
import com.example.manageproxies.app.presentation.usecase.GetModemApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetModemIpApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetServerApiUsecase
import com.example.manageproxies.app.presentation.usecase.GetTokenUsecase
import com.example.manageproxies.app.presentation.usecase.SaveServerToDatabaseUsecase
import com.example.manageproxies.app.presentation.usecase.SaveTokenUsecase
import com.example.manageproxies.app.presentation.usecase.ScheduledSavingServerInfoToDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveTokenUseCase: SaveTokenUsecase,
    private val getTokenUseCase: GetTokenUsecase,
    private val getServerApiUseCase: GetServerApiUsecase,
    private val getModemApiUseCase: GetModemApiUsecase,
    private val getModemIpApiUseCase: GetModemIpApiUsecase,
    private val saveServerToDatabaseUsecase: SaveServerToDatabaseUsecase,
    private val scheduledSavingServerInfoToDatabaseUseCase: ScheduledSavingServerInfoToDatabaseUseCase,
    private val getDailyStatisticFromDatabase: GetDailyStatisticFromDatabase
) : ViewModel() {


    private val _serverInfo = MutableLiveData<List<ServerUi>>()
    val serverInfo: LiveData<List<ServerUi>> = _serverInfo

    private val _dailyStatistic = MutableLiveData<DailyStatistic>()
    val dailyStatistic: LiveData<DailyStatistic> = _dailyStatistic

    private val _modems = MutableLiveData<List<ModemUi>>()
    val modems: LiveData<List<ModemUi>> = _modems

    private val _orders = MutableLiveData<Int>()
    val orders: LiveData<Int> = _orders

    private val _selfOrders = MutableLiveData<Int>()
    val selfOrders: LiveData<Int> = _selfOrders

    private val _allOrders = MutableLiveData<Int>()
    val allOrders: LiveData<Int> = _allOrders

    private val _testOrders = MutableLiveData<Int>()
    val testOrders: LiveData<Int> = _testOrders

    init {
        scheduledSavingServerInfoToDatabaseUseCase.scheduleDailyWork()
        viewModelScope.launch(Dispatchers.IO) {
            getServerApi()
            getModemApi()
            getDailyStatistic()


            withContext(Dispatchers.Main) {
                _orders.value = modems.value?.count { it.order != null } ?: 0
                _selfOrders.value = modems.value?.count { it.selfOrder == true } ?: 0
                _testOrders.value = modems.value?.count { it.testOrder == true } ?: 0
                _allOrders.value = orders.value?.plus(selfOrders.value!!) ?: 0
            }
        }

    }

    fun saveToken(token: Token) {
        saveTokenUseCase.execute(token)

    }

    fun getToken(): Token {
        return getTokenUseCase.execute()
    }

    suspend fun getDailyStatistic() {
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        _dailyStatistic.postValue(getDailyStatisticFromDatabase.execute(currentDate))
    }

    suspend fun getServerApi() {
        try {
            val serverInfo = getServerApiUseCase.execute().map { it.toServerUi() }
            _serverInfo.postValue(serverInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getModemApi() {
        try {
            val modems = getModemApiUseCase.execute().map { it.toModemUi() }
                .sortedByDescending { it.order != null }
            _modems.postValue(modems)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setModemStatusNew() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentModems = modems.value
                if (currentModems.isNullOrEmpty()) {
                    return@launch
                }
                val eids = currentModems.map { it.eid }
                val eidsString = eids.joinToString(",")
                val allIps = getModemIpApiUseCase.execute(eidsString).toModemIpUi()
                val ipsMap = allIps.eid ?: emptyMap()
                val updatedModems = currentModems.map { modem ->
                    if (ipsMap.containsKey(modem.eid.toString())) {
                        modem.copy(status = true)
                    } else modem
                }
                _modems.postValue(updatedModems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}
