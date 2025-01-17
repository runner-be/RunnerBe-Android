package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.GatheringData
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLog
import com.applemango.runnerbe.entity.TotalCount
import com.applemango.runnerbe.presentation.mapper.MonthlyRunningLogMapper
import com.applemango.runnerbe.presentation.model.MonthlyRunningLogsModel
import com.applemango.runnerbe.presentation.model.RunningLogModel
import javax.inject.Inject

class MonthlyRunningLogMapperImpl @Inject constructor(): MonthlyRunningLogMapper {
    override fun mapToDomain(input: MonthlyRunningLogsModel): MonthlyRunningLogEntity {
        return MonthlyRunningLogEntity(
            totalCount = TotalCount(
                input.totalCount?.groupRunningCount ?: 0,
                input.totalCount?.personalRunningCount ?: 0,
            ),
            runningLog = input.runningLog.map {
                RunningLog(
                    it.logId,
                    it.gatheringId,
                    it.runnedDate,
                    it.stampCode,
                    it.isOpened
                )

            },
            gatheringDays = input.gatheringDays.map {
                GatheringData(
                    it.gatheringId,
                    it.date
                )
            },
        )
    }

    override fun mapToPresentation(input: MonthlyRunningLogEntity): MonthlyRunningLogsModel {
        return MonthlyRunningLogsModel(
            totalCount = com.applemango.runnerbe.presentation.model.TotalCount(
                input.totalCount?.groupRunningCount ?: 0,
                input.totalCount?.personalRunningCount ?: 0,
            ),
            runningLog = input.runningLog.map {
                RunningLogModel(
                    it.logId,
                    it.gatheringId,
                    it.runnedDate,
                    it.stampCode,
                    it.isOpened
                )

            },
            gatheringDays = input.gatheringDays.map {
                com.applemango.runnerbe.presentation.model.GatheringData(
                    it.gatheringId,
                    it.date
                )
            },
        )
    }
}