package kr.devkyu.domain.usecaseImpl.runningtalk

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.RunningTalkRepository
import javax.inject.Inject

class ReportMessageUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    suspend operator fun invoke(messageIdList : List<Int>) : BaseEntity {
        return repo.reportMessage(messageIdList)
    }
}