package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.MemberStamp
import com.applemango.domain.entity.RunningLogDetailEntity
import com.applemango.presentation.ui.mapper.RunningLogDetailMapper
import com.applemango.presentation.ui.model.MemberStampModel
import com.applemango.presentation.ui.model.RunningLogDetail
import com.applemango.presentation.ui.model.RunningLogDetailModel
import javax.inject.Inject

class RunningLogDetailMapperImpl @Inject constructor(): RunningLogDetailMapper {

    override fun mapToPresentation(input: RunningLogDetailEntity): RunningLogDetailModel {
        return RunningLogDetailModel(
            runningLog = RunningLogDetail(
                status = input.runningLog.status,
                runnedDate = input.runningLog.runnedDate,
                userId = input.runningLog.userId,
                nickname = input.runningLog.nickname,
                gatheringId = input.runningLog.gatheringId,
                stampCode = input.runningLog.stampCode,
                contents = input.runningLog.contents,
                imageUrl = input.runningLog.imageUrl,
                weatherDegree = input.runningLog.weatherDegree,
                weatherIcon = input.runningLog.weatherIcon,
                isOpened = input.runningLog.isOpened,
            ),
            gatheringCount = input.gatheringCount,
            gotStamp = input.gotStamp.map {
                MemberStampModel(
                    userId = it.userId,
                    logId = it.logId,
                    nickname = it.nickname,
                    profileImageUrl = it.profileImageUrl,
                    stampCode = it.stampCode,
                )
            }
        )
    }

    override fun mapToDomain(input: RunningLogDetailModel): RunningLogDetailEntity {
        return RunningLogDetailEntity(
            runningLog = com.applemango.domain.entity.RunningLogDetail(
                status = input.runningLog.status,
                runnedDate = input.runningLog.runnedDate,
                userId = input.runningLog.userId,
                nickname = input.runningLog.nickname,
                gatheringId = input.runningLog.gatheringId,
                stampCode = input.runningLog.stampCode,
                contents = input.runningLog.contents,
                imageUrl = input.runningLog.imageUrl,
                weatherDegree = input.runningLog.weatherDegree,
                weatherIcon = input.runningLog.weatherIcon,
                isOpened = input.runningLog.isOpened,
            ),
            gatheringCount = input.gatheringCount,
            gotStamp = input.gotStamp.map {
                MemberStamp(
                    userId = it.userId,
                    logId = it.logId,
                    nickname = it.nickname,
                    profileImageUrl = it.profileImageUrl,
                    stampCode = it.stampCode,
                )
            }
        )
    }
}