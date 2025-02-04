package com.applemango.data.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AttendanceAccessionRequest(
    @Json(name = "userIdList") val userIds: String, //"1,2,3" 형태의 String으로 서버에서 받음
    @Json(name = "whetherAttendList") val attendList : String // "Y,N" 형태의 String으로 서버에서 받음
)
