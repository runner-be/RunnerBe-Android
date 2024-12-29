package com.applemango.runnerbe.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

// 마이페이지(UserInfo), 게시글 상세(runnerInfo, waitingRunnerInfo)
@Parcelize
data class UserInfo(
    @Json(name = "userId") val userId: Int,
    @Json(name = "nickName") val nickName: String?,
    @Json(name = "gender") val gender: String?,
    @Json(name = "age") val age: String?,
    @Json(name = "diligence") val diligence: String?,
    @Json(name = "job") val job: String?,
    @Json(name = "pace") val pace: String?,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    // Y : 수신동의, N : 수신거부
    @Json(name = "pushOn") val pushOn: String?,
    // 게시글 상세 Y: 출석체크 실시함, N: 출석체크 아직 안함
    @Json(name = "whetherCheck") val whetherCheck: String?,
    // 1: 출석O, 0: 출석X
    @Json(name = "attendance") var attendance: Int?,
    var attendanceState : Boolean? = null, // -1인경우 최초 선택 하지 않음
    // 게시글 상세(작성자에서만) N: 대기중인 러너
    @Json(name = "whetherAccept") val whetherAccept: String?,
    // 닉네임 변경되었는지
    @Json(name = "nameChanged") val nameChanged: String?,
    // 직군변경할 수 있는지 Y : 가능 N : 불가능
    @Json(name = "jobChangePossible") val jobChangePossible: String?
) : Parcelable