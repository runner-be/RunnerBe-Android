package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName

data class JoinUserRequest(
    @SerializedName("uuid") val uuid : String,
    @SerializedName("nickName") val nickName : String? = null,
    @SerializedName("birthday") val birthday: Int,
    @SerializedName("gender") val genderTag : String,
    @SerializedName("job") val jobTag : String,
    @SerializedName("deviceToken") val deviceToken : String
)
