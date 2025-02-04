package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.JoinedRunnerEntity
import com.applemango.presentation.ui.mapper.JoinedRunnerMapper
import com.applemango.presentation.ui.model.JoinedRunnerModel
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