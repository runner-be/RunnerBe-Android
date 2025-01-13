package com.applemango.runnerbe.data.dto.new

import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "isSuccess") val isSuccess: Boolean = false,
    @Json(name = "code") val code: Int = 0,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result : User
)

data class User (
    val userId: Int,
    val nickName: String?,
    val gender: String?,
    val age: String?,
    val diligence: String?,
    val job: String?,
    val pace: String?,
    val profileImageUrl: String?,
    val pushOn: String?, // Y : 수신동의, N : 수신거부
    val whetherCheck: String?, // 게시글 상세 Y: 출석체크 실시함, N: 출석체크 아직 안함
    var attendance: Int?, // 1: 출석O, 0: 출석X
    var attendanceState : Boolean? = null, // -1인경우 최초 선택 하지 않음
    val whetherAccept: String?, // 게시글 상세(작성자에서만) N: 대기중인 러너
    val nameChanged: String?, // 닉네임 변경되었는지
    val jobChangePossible: String? // 직군변경할 수 있는지 Y : 가능 N : 불가능
)