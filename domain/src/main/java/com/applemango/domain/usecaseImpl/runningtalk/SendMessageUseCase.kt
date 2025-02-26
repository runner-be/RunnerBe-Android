package com.applemango.domain.usecaseImpl.runningtalk

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.RunningTalkRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: RunningTalkRepository
) {
    suspend operator fun invoke(roomId: Int, content: String?, url: String?): CommonEntity {
        return repository.sendMessage(roomId, content, url)
    }
}