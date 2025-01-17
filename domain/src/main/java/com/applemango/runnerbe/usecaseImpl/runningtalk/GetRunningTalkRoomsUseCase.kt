package com.applemango.runnerbe.usecaseImpl.runningtalk

import com.applemango.runnerbe.entity.RunningTalkRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.repository.RunningTalkRepository
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