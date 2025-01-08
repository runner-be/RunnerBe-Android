package kr.devkyu.domain.usecaseImpl.runningtalk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.RunningTalkRoomsEntity
import kr.devkyu.domain.repository.RunningTalkRepository
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