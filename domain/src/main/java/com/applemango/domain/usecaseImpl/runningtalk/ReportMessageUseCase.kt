package com.applemango.domain.usecaseImpl.runningtalk

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.RunningTalkRepository
import javax.inject.Inject

class ReportMessageUseCase @Inject constructor(
    private val repository: RunningTalkRepository
) {
    suspend operator fun invoke(messageIdList : List<Int>) : CommonEntity {
        return repository.reportMessage(messageIdList)
    }
}