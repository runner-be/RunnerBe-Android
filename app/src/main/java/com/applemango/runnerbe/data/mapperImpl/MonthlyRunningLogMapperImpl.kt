package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.GatheringData
import com.applemango.runnerbe.data.dto.MonthlyRunningLog
import com.applemango.runnerbe.data.dto.MonthlyRunningLogDto
import com.applemango.runnerbe.data.dto.RunningLogDto
import com.applemango.runnerbe.data.dto.TotalCount
import com.applemango.runnerbe.data.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLog
import javax.inject.Inject

class MonthlyRunningLogMapperImpl @Inject constructor() : MonthlyRunningLogMapper {
    override fun mapToData(input: MonthlyRunningLogEntity): MonthlyRunningLogDto {
        return MonthlyRunningLogDto(
            result = MonthlyRunningLog(
                totalCount = TotalCount(
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
                    GatheringData(
                        it.gatheringId,
                        it.date,
                    )
                }
            )
        )
    }

    override fun mapToDomain(input: MonthlyRunningLogDto): MonthlyRunningLogEntity {
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