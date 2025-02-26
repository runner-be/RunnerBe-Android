package com.applemango.domain.usecaseImpl.runningtalk

import com.applemango.domain.entity.RunningTalkRoomEntity
import com.applemango.domain.repository.RunningTalkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRunningTalkRoomsUseCase @Inject constructor(
    private val repo : RunningTalkRepository
) {
    operator fun invoke() : Flow<List<RunningTalkRoomEntity>> = flow {
        runCatching {
            repo.getRunningTalkRooms()
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}