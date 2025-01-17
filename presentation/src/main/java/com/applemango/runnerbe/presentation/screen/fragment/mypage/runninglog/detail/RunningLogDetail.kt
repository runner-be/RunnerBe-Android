package com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.detail

import android.os.Parcelable
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
    val weatherDegree: Int?,
    val weatherCode: String?,
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