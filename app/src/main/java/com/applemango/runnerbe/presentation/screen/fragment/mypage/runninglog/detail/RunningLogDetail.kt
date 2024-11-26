package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import android.os.Parcelable
import com.applemango.runnerbe.data.network.response.DetailRunningLogResponse
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class RunningLogDetail(
    val runningLog: RunningLogData,
    val gatheringCount: Int,
    val gotStamp: List<MemberStampData>
) : Parcelable

@Parcelize
data class RunningLogData (
    val status: String,
    val runnedDate: ZonedDateTime,
    val userId: Int,
    val nickname: String,
    val gatheringId: Int?,
    val stampCode: String,
    val contents: String,
    val imageUrl: String?,
    val weatherDegree: Int,
    val weatherCode: String,
    val isOpened: Int,
) : Parcelable

@Parcelize
data class MemberStampData (
    val userId: Int,
    val logId: Int?,
    val nickname: String,
    val profileImageUrl: String?,
    val stampCode: String,
) : Parcelable

fun parseRunningLogDetailToPresentation(runningLogResponse: DetailRunningLogResponse): RunningLogDetail {
    val runningLog = runningLogResponse.result.runningLog
    return RunningLogDetail(
        runningLog = RunningLogData(
            status = runningLog.status,
            runnedDate = runningLog.runnedDate,
            userId = runningLog.userId,
            nickname = runningLog.nickname,
            gatheringId = runningLog.gatheringId,
            stampCode = runningLog.stampCode,
            contents = runningLog.contents,
            imageUrl = runningLog.imageUrl,
            weatherDegree = runningLog.weatherDegree,
            weatherCode = runningLog.weatherCode,
            isOpened = runningLog.isOpened,
        ),
        gatheringCount = runningLogResponse.result.gatheringCount,
        gotStamp = runningLogResponse.result.gotStamp.map {
            MemberStampData(
                it.userId,
                it.logId,
                it.nickname,
                it.profileImageUrl,
                it.stampCode
            )
        }
    )
}