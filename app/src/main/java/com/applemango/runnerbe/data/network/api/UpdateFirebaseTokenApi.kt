package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.FirebaseTokenUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UpdateFirebaseTokenApi {

    @PATCH("/users/{userId}/deviceToken")
    suspend fun firebaseTokenUpdate(
        @Path("userId") userId: Int,
        @Body request: FirebaseTokenUpdateRequest
    ): Response<CommonDto>
}