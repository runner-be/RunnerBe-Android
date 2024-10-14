package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostWhetherAcceptUseCase @Inject constructor(
    private val repo: PostRepository
) {

    operator fun invoke(postId : Int, applicantId: Int, whetherAccept : String) : Flow<CommonResponse> = flow {
        if(whetherAccept == "Y" || whetherAccept =="D") {
            runCatching {
                emit(CommonResponse.Loading)
                repo.postWhetherAccept(postId, applicantId, whetherAccept)
            }.onSuccess {
                emit(it)
            }.onFailure {
                it.printStackTrace()
                emit(CommonResponse.Failed.getDefaultFailed(it.message))
            }
        } else {
            emit(CommonResponse.Failed(700, "문제가 발생했습니다."))
        }
    }
}