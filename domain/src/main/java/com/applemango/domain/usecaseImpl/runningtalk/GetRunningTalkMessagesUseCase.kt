package com.applemango.domain.usecaseImpl.runningtalk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.domain.entity.RunningTalkMessagesEntity
import com.applemango.domain.repository.RunningTalkRepository
import javax.inject.Inject

class GetRunningTalkMessagesUseCase @Inject constructor(
    private val repo : RunningTalkRepository
) {
    operator fun invoke(roomId: Int) :Flow<RunningTalkMessagesEntity> = flow {
        runCatching {
            repo.getRunningTalkMessages(roomId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}