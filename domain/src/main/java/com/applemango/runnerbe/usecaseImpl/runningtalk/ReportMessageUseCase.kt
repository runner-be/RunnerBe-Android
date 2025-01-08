package com.applemango.runnerbe.usecaseImpl.runningtalk

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import javax.inject.Inject

class ReportMessageUseCase @Inject constructor(
    private val repo: RunningTalkRepository
) {
    suspend operator fun invoke(messageIdList : List<Int>) : BaseEntity {
        return repo.reportMessage(messageIdList)
    }
}