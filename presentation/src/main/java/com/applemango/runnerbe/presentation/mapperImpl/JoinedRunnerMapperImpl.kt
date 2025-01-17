package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.JoinedRunnerEntity
import com.applemango.runnerbe.presentation.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import javax.inject.Inject

class JoinedRunnerMapperImpl @Inject constructor(): JoinedRunnerMapper {
    override fun mapToDomain(input: JoinedRunnerModel): JoinedRunnerEntity {
        return JoinedRunnerEntity(
            userId = input.userId,
            nickname = input.nickname,
            profileImageUrl = input.profileImageUrl,
            logId = input.logId,
            isOpened = input.isOpened,
            stampCode = input.stampCode,
            isCaptain = input.isCaptain,
        )
    }

    override fun mapToPresentation(input: JoinedRunnerEntity): JoinedRunnerModel {
        return JoinedRunnerModel(
            userId = input.userId,
            nickname = input.nickname,
            profileImageUrl = input.profileImageUrl,
            logId = input.logId,
            isOpened = input.isOpened,
            stampCode = input.stampCode,
            isCaptain = input.isCaptain,
        )
    }
}