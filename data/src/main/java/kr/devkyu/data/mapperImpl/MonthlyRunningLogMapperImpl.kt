package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.GatheringData
import kr.devkyu.data.dto.MonthlyRunningLog
import kr.devkyu.data.dto.MonthlyRunningLogDto
import com.applemango.runnerbe.data.dto.RunningLogDto
import kr.devkyu.data.dto.TotalCount
import kr.devkyu.data.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLog
import javax.inject.Inject

class MonthlyRunningLogMapperImpl @Inject constructor() :
    MonthlyRunningLogMapper {
    override fun mapToData(input: MonthlyRunningLogEntity): kr.devkyu.data.dto.MonthlyRunningLogDto {
        return kr.devkyu.data.dto.MonthlyRunningLogDto(
            result = kr.devkyu.data.dto.MonthlyRunningLog(
                totalCount = kr.devkyu.data.dto.TotalCount(
                    input.totalCount?.groupRunningCount ?: 0,
                    input.totalCount?.personalRunningCount ?: 0,
                ),
                runningLog = input.runningLog.map {
                    RunningLogDto(
                        it.logId,
                        it.gatheringId,
                        it.runnedDate,
                        it.stampCode,
                        it.isOpened,
                    )
                },
                gatheringDays = input.gatheringDays.map {
                    kr.devkyu.data.dto.GatheringData(
                        it.gatheringId,
                        it.date,
                    )
                }
            )
        )
    }

    override fun mapToDomain(input: kr.devkyu.data.dto.MonthlyRunningLogDto): MonthlyRunningLogEntity {
        val data = input.result
        return MonthlyRunningLogEntity(
            totalCount = com.applemango.runnerbe.entity.TotalCount(
                data.totalCount?.groupRunningCount ?: 0,
                data.totalCount?.personalRunningCount ?: 0,
            ),
            runningLog = data.runningLog.map {
                RunningLog(
                    it.logId,
                    it.gatheringId,
                    it.runnedDate,
                    it.stampCode,
                    it.isOpened,
                )
            },
            gatheringDays = data.gatheringDays.map {
                com.applemango.runnerbe.entity.GatheringData(
                    it.gatheringId,
                    it.date,
                )
            }
        )
    }
}