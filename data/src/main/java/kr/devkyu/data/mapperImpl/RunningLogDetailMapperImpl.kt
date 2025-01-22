package kr.devkyu.data.mapperImpl

import kr.devkyu.data.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import kr.devkyu.data.dto.RunningLogDetailDto
import javax.inject.Inject

class RunningLogDetailMapperImpl @Inject constructor():
    RunningLogDetailMapper {
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