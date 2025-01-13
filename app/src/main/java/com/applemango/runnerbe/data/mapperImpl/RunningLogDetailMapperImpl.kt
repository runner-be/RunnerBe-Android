package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.RunningLogDetailDto
import com.applemango.runnerbe.data.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.entity.RunningLogDetailEntity

class RunningLogDetailMapperImpl: RunningLogDetailMapper {
    override fun mapToData(input: RunningLogDetailEntity): RunningLogDetailDto {
        return RunningLogDetailDto(
            runningLog = input.runningLog,
            gotStamp = input.gotStamp,
            gatheringCount = input.gatheringCount,
        )
    }

    override fun mapToDomain(input: RunningLogDetailDto): RunningLogDetailEntity {
        return RunningLogDetailEntity(
            runningLog = input.runningLog,
            gotStamp = input.gotStamp,
            gatheringCount = input.gatheringCount,
        )
    }
}