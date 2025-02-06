package com.applemango.presentation.ui.screen.fragment.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applemango.domain.usecaseImpl.runningtalk.GetRunningTalkRoomsUseCase
import com.applemango.presentation.ui.mapper.RunningTalkRoomMapper
import com.applemango.presentation.ui.model.RunningTalkRoomModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningTalkViewModel @Inject constructor(
    private val getRunningTalkRoomsUseCase: GetRunningTalkRoomsUseCase,
    private val runningTalkRoomMapper: RunningTalkRoomMapper
): ViewModel() {

    val roomList : MutableStateFlow<List<RunningTalkRoomModel>> = MutableStateFlow(emptyList())

    fun getRunningTalkList() {
        viewModelScope.launch {
            getRunningTalkRoomsUseCase().collect {
                roomList.value = it.map { room ->
                    runningTalkRoomMapper.mapToPresentation(room)
                }
            }
        }
    }
}