package com.applemango.runnerbe.domain.usecase.runningtalk

import com.applemango.runnerbe.domain.repository.RunningTalkRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import javax.inject.Inject

class MessageSendUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    suspend operator fun invoke(roomId: Int, content: String?, url: String?): CommonResponse {
        runCatching {
            repo.sendMessage(roomId, content, url)
        }.onSuccess {
            return it
        }.onFailure {
            it.printStackTrace()
            return CommonResponse.Failed(999, it.message?:"error")
        }
        return CommonResponse.Failed(999, "error")
    }
}