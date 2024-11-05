package com.applemango.runnerbe.presentation.screen.fragment.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.data.dto.Room
import com.applemango.runnerbe.domain.usecase.GetRunningTalkUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.data.network.response.RunningTalksResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningTalkViewModel @Inject constructor(
    private val runningTalkUseCase: GetRunningTalkUseCase
): ViewModel() {

    val roomList : MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())

    fun getRunningTalkList() {
        viewModelScope.launch {
            runningTalkUseCase().collect {
                if(it is CommonResponse.Success<*> && it.body is RunningTalksResponse) {
                    roomList.value = it.body.result
                }
            }
        }
    }
}