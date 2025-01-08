package kr.devkyu.domain.usecaseImpl.runningtalk

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningTalkRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    suspend operator fun invoke(roomId: Int, content: String?, url: String?): BaseEntity {
        return repo.sendMessage(roomId, content, url)
    }
}