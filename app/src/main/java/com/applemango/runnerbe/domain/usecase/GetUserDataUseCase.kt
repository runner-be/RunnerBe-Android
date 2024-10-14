package com.applemango.runnerbe.domain.usecase

import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repo : UserRepository) {

    operator fun invoke(userId: Int) :Flow<CommonResponse> = flow {
        repo.getUserData(userId).run {
            if(this.body() != null) {
                emit(CommonResponse.Success(this.body()!!.code, this.body()))
            } else {
                emit(CommonResponse.Failed(this.body()?.code?:this.code(), this.body()?.message?:this.message()))
            }
        }
    }.catch { e ->
        e.printStackTrace()
        emit(CommonResponse.Failed.getDefaultFailed(e.message))
    }
}