package com.applemango.runnerbe.usecaseImpl.runningtalk

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: RunningTalkRepository
) {
    suspend operator fun invoke(roomId: Int, content: String?, url: String?): CommonEntity {
        return repository.sendMessage(roomId, content, url)
    }
}