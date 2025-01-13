package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.MonthlyRunningLogDto
import com.applemango.runnerbe.data.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity

class MonthlyRunningLogMapperImpl: MonthlyRunningLogMapper {
    override fun mapToData(input: MonthlyRunningLogEntity): MonthlyRunningLogDto {
        return MonthlyRunningLogDto(
            totalCount = input.totalCount,
            runningLog = input.runningLog,
            gatheringDays = input.gatheringDays
        )
    }

    override fun mapToDomain(input: MonthlyRunningLogDto): MonthlyRunningLogEntity {
        return MonthlyRunningLogEntity(
            totalCount = input.totalCount,
            runningLog = input.runningLog,
            gatheringDays = input.gatheringDays
        )
    }
}