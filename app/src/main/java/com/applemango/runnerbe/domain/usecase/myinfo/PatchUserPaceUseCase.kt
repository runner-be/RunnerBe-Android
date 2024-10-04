package com.applemango.runnerbe.domain.usecase.myinfo

import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatchUserPaceUseCase @Inject constructor(private val repo : UserRepository)  {

    operator fun invoke(userId: Int, pace: Pace)  : Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.patchUserPaceRegist(userId, pace.key)
        }.onSuccess {
            emit(it)
        }.onFailure { e->
            e.printStackTrace()
            emit(CommonResponse.Failed(999, e.message?:"error"))
        }
    }
}