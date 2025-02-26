package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.GatheringData
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.entity.RunningLog
import com.applemango.domain.entity.TotalCount
import com.applemango.presentation.ui.mapper.MonthlyRunningLogMapper
import com.applemango.presentation.ui.model.MonthlyRunningLogsModel
import com.applemango.presentation.ui.model.RunningLogModel
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
            totalCount = com.applemango.presentation.ui.model.TotalCount(
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
                com.applemango.presentation.ui.model.GatheringData(
                    it.gatheringId,
                    it.date
                )
            },
        )
    }
}