package com.applemango.data.mapperImpl

import com.applemango.data.dto.JoinedRunnerDto
import com.applemango.data.mapper.JoinedRunnerMapper
import com.applemango.domain.entity.JoinedRunnerEntity
import javax.inject.Inject

class JoinedRunnerMapperImpl @Inject constructor():
    JoinedRunnerMapper {
    override fun mapToData(input: JoinedRunnerEntity): JoinedRunnerDto {
        return JoinedRunnerDto(
            userId = input.userId,
            nickname = input.nickname,
            profileImageUrl = input.profileImageUrl,
            logId = input.logId,
            isOpened = input.isOpened,
            stampCode = input.stampCode,
            isCaptain = input.isCaptain,
        )
    }

    override fun mapToDomain(input: JoinedRunnerDto): JoinedRunnerEntity {
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
}