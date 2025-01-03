package com.applemango.runnerbe.presentation.screen.fragment.main.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.network.response.GetNotificationsResponse
import com.applemango.runnerbe.data.network.response.Alarm
import com.applemango.runnerbe.domain.usecase.alarm.GetAlarmsUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.util.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val getAlarmsUseCase: GetAlarmsUseCase
): ViewModel() {

    private val _notificationsFlow: MutableStateFlow<List<Alarm>> = MutableStateFlow(emptyList())
    val notificationsFlow: StateFlow<List<Alarm>> get() = _notificationsFlow.asStateFlow()

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            getAlarmsUseCase().collectLatest { response ->
                when (response) {
                    is CommonResponse.Success<*> -> {
                        if (response.body is GetNotificationsResponse) {
                            val result = response.body.alarms as? List<Alarm> ?: emptyList()
                            _notificationsFlow.value = result.sortedByDescending { it.createdAt }
                        }
                    }

                    is CommonResponse.Failed -> {
                        throw Exception(response.message)
                    }

                    else -> {
                        LogUtil.errorLog(response.toString())
                    }
                }
            }
        }
    }
}