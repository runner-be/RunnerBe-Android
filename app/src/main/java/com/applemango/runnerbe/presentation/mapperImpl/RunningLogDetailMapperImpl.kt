package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.MemberStamp
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.presentation.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.presentation.model.MemberStampModel
import com.applemango.runnerbe.presentation.model.RunningLogDetail
import com.applemango.runnerbe.presentation.model.RunningLogDetailModel
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
                weatherCode = input.runningLog.weatherCode,
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
            runningLog = com.applemango.runnerbe.entity.RunningLogDetail(
                status = input.runningLog.status,
                runnedDate = input.runningLog.runnedDate,
                userId = input.runningLog.userId,
                nickname = input.runningLog.nickname,
                gatheringId = input.runningLog.gatheringId,
                stampCode = input.runningLog.stampCode,
                contents = input.runningLog.contents,
                imageUrl = input.runningLog.imageUrl,
                weatherDegree = input.runningLog.weatherDegree,
                weatherCode = input.runningLog.weatherCode,
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