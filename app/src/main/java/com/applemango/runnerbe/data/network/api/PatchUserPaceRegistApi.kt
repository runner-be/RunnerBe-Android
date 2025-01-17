package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.PatchUserPaceRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchUserPaceRegistApi {

    @PATCH("/users/{userId}/pace")
    suspend fun patchUserPaceRegist(
        @Path("userId") userId: Int,
        @Body pace: PatchUserPaceRegisterRequest
    ): Response<CommonDto>
}