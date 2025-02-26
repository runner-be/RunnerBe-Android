package com.applemango.data.mapperImpl

import com.applemango.data.dto.GatheringData
import com.applemango.data.dto.MonthlyRunningLog
import com.applemango.data.dto.RunningLogDto
import com.applemango.data.mapper.MonthlyRunningLogMapper
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.entity.RunningLog
import com.applemango.domain.entity.TotalCount
import javax.inject.Inject
import com.applemango.data.dto.MonthlyRunningLogDto as MonthlyRunningLogDto1

class MonthlyRunningLogMapperImpl @Inject constructor() :
    MonthlyRunningLogMapper {
    override fun mapToData(input: MonthlyRunningLogEntity): MonthlyRunningLogDto1 {
        return MonthlyRunningLogDto1(
            result = MonthlyRunningLog(
                totalCount = com.applemango.data.dto.TotalCount(
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

    override fun mapToDomain(input: MonthlyRunningLogDto1): MonthlyRunningLogEntity {
        val data = input.result
        return MonthlyRunningLogEntity(
            totalCount = TotalCount(
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
                com.applemango.domain.entity.GatheringData(
                    it.gatheringId,
                    it.date,
                )
            }
        )
    }
}