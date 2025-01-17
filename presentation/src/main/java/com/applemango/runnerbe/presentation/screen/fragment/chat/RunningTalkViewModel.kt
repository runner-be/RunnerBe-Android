package com.applemango.runnerbe.presentation.screen.fragment.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.runnerbe.presentation.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.presentation.model.RunningTalkRoomModel
import com.applemango.runnerbe.usecaseImpl.runningtalk.GetRunningTalkRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningTalkViewModel @Inject constructor(
    private val runningTalkUseCase: GetRunningTalkRoomsUseCase,
    private val runningTalkRoomMapper: RunningTalkRoomMapper
): ViewModel() {

    val roomList : MutableStateFlow<List<RunningTalkRoomModel>> = MutableStateFlow(emptyList())

    fun getRunningTalkList() {
        viewModelScope.launch {
            runningTalkUseCase().collect {
                roomList.value = it.map { room ->
                    runningTalkRoomMapper.mapToPresentation(room)
                }
            }
        }
    }
}