package com.applemango.runnerbe.presentation.screen.fragment.main.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.AlarmMapper
import com.applemango.runnerbe.presentation.model.AlarmModel
import com.applemango.runnerbe.usecaseImpl.alarm.GetAlarmsUseCase
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
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val alarmMapper: AlarmMapper
): ViewModel() {

    private val _notificationsFlow: MutableStateFlow<List<AlarmModel>> = MutableStateFlow(emptyList())
    val notificationsFlow: StateFlow<List<AlarmModel>> get() = _notificationsFlow.asStateFlow()

    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            getAlarmsUseCase().collectLatest { response ->
                val result = response.map {
                    alarmMapper.mapToPresentation(it)
                }
                _notificationsFlow.value = result
            }
        }
    }
}