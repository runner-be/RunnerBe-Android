package com.applemango.runnerbe.usecaseImpl.runningtalk

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import javax.inject.Inject

class ReportMessageUseCase @Inject constructor(
    private val repository: RunningTalkRepository
) {
    suspend operator fun invoke(messageIdList : List<Int>) : CommonEntity {
        return repository.reportMessage(messageIdList)
    }
}