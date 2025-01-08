package com.applemango.runnerbe.usecaseImpl.runningtalk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.runnerbe.entity.RunningTalkRoomsEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import javax.inject.Inject

class GetRunningTalkRoomsUseCase @Inject constructor(
    private val repo : RunningTalkRepository
) {
    operator fun invoke() : Flow<RunningTalkRoomsEntity> = flow {
        runCatching {
            repo.getRunningTalkRooms()
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}