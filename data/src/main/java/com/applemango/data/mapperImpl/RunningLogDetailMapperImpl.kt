package com.applemango.data.mapperImpl

import com.applemango.data.dto.RunningLogDetailData
import com.applemango.data.dto.RunningLogDetailDto
import com.applemango.data.mapper.RunningLogDetailMapper
import com.applemango.domain.entity.RunningLogDetailEntity
import javax.inject.Inject

class RunningLogDetailMapperImpl @Inject constructor():
    RunningLogDetailMapper {
    override fun mapToData(input: RunningLogDetailEntity): RunningLogDetailDto {
        return RunningLogDetailDto(
            result = RunningLogDetailData(
                runningLog = input.runningLog,
                gotStamp = input.gotStamp,
                gatheringCount = input.gatheringCount,
            )
        )
    }

    override fun mapToDomain(input: RunningLogDetailDto): RunningLogDetailEntity {
        return RunningLogDetailEntity(
            runningLog = input.result.runningLog,
            gotStamp = input.result.gotStamp,
            gatheringCount = input.result.gatheringCount,
        )
    }
}