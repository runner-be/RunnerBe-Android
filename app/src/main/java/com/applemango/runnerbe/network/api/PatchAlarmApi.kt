package com.applemango.runnerbe.network.api

import com.applemango.runnerbe.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchAlarmApi {
    @PATCH("/users/{userId}/push-alarm/{pushOn}")
    suspend fun patchAlarm(@Path("userId") userId: Int, @Path("pushOn") pushOn: String) : Response<BaseResponse>
}