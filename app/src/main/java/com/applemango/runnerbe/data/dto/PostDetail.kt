package com.applemango.runnerbe.data.dto

import com.squareup.moshi.Json

data class PostDetail(
    @Json(name = "postingInfo") val postList : List<Posting>,
    @Json(name = "runnerInfo") val runnerInfo : List<UserInfo>?,
    @Json(name = "waitingRunnerInfo") val waitingRunnerInfo : List<UserInfo>?,
    @Json(name = "roomId") val roomId: Int
)
