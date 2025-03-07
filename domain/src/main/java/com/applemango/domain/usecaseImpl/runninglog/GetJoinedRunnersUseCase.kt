package com.applemango.domain.usecaseImpl.runninglog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.applemango.domain.entity.JoinedRunnerEntity
import com.applemango.domain.repository.JoinedRunnerRepository
import javax.inject.Inject

class GetJoinedRunnersUseCase @Inject constructor(
    private val repository: JoinedRunnerRepository
) {
    operator fun invoke(gatheringId: Int): Flow<List<JoinedRunnerEntity>> = flow {
        kotlin.runCatching {
            repository.getJoinedRunnerList(gatheringId)
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
        }
    }
}