package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.JoinedRunnerDto
import com.applemango.runnerbe.data.dto.new.JoinedRunnersResponse
import com.applemango.runnerbe.data.mapper.JoinedRunnersMapper
import com.applemango.runnerbe.entity.JoinedRunnersEntity

class JoinedRunnersMapperImpl: JoinedRunnersMapper {
    override fun mapToData(input: List<JoinedRunnersEntity>): List<JoinedRunnerDto> {
        return input.map {
            JoinedRunnerDto(
                it.userId,
                it.nickname,
                it.profileImageUrl,
                it.logId,
                it.isOpened,
                it.stampCode,
                it.isCaptain,
            )
        }
    }

    override fun mapToDomain(input: List<JoinedRunnerDto>): List<JoinedRunnersEntity> {
        return input.map {
            JoinedRunnersEntity(
                it.userId,
                it.nickname,
                it.profileImageUrl,
                it.logId,
                it.isOpened,
                it.stampCode,
                it.isCaptain,
            )
        }
    }
}