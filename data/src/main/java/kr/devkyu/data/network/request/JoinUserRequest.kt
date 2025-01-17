package kr.devkyu.data.network.request

import com.squareup.moshi.Json

data class JoinUserRequest(
    @Json(name = "uuid") val uuid : String,
    @Json(name = "nickName") val nickName : String? = null,
    @Json(name = "birthday") val birthday: Int,
    @Json(name = "gender") val genderTag : String,
    @Json(name = "job") val jobTag : String,
    @Json(name = "deviceToken") val deviceToken : String
)
