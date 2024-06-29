package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.GetPostDetailResponse
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(private val repo: PostRepository) {

    operator fun invoke(postId: Int, userId: Int): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.getPostDetail(postId, userId)
        }.onSuccess {
            if (it is CommonResponse.Success<*>) {
                if (it.body is GetPostDetailResponse) {
                    if(it.body.result.postList.isNotEmpty()) {
                        emit(
                            CommonResponse.Success(
                                it.code, PostDetailManufacture(
                                    it.body.code,
                                    it.body.result.postList[0],
                                    it.body.result.runnerInfo,
                                    it.body.result.waitingRunnerInfo,
                                    it.body.result.roomId
                                )
                            )
                        )
                    } else emit(CommonResponse.Failed(it.code, it.body.message?: "error"))
                } else emit(CommonResponse.Failed(it.code, (it.body as BaseResponse ).message?: "error"))
            }else {
                emit(it)
            }
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed(999, it.message ?: "error"))
        }
    }
}

data class PostDetailManufacture(
    val code: Int,
    val post: Posting,
    val runnerInfo: List<UserInfo>?,
    val waitingInfo: List<UserInfo>?,
    val roomId: Int
)