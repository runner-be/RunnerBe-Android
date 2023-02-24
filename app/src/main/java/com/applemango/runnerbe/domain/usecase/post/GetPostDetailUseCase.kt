package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.data.dto.UserInfo
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
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
                    emit(
                        CommonResponse.Success(
                            it.code, PostDetailManufacture(
                                it.body.result.postList[0],
                                it.body.result.runnerInfo,
                                it.body.result.waitingRunnerInfo
                            )
                        )
                    )
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
    val post: Posting,
    val runnerInfo: List<UserInfo>,
    val waitingInfo: List<UserInfo>
)