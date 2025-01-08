package com.applemango.runnerbe.usecaseImpl.runningtalk

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    suspend operator fun invoke(roomId: Int, content: String?, url: String?): BaseEntity {
        return repo.sendMessage(roomId, content, url)
    }
}