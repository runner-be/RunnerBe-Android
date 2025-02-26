package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.FirebaseTokenUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UpdateFirebaseTokenApi {

    @PATCH("/users/{userId}/deviceToken")
    suspend fun firebaseTokenUpdate(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Body request: FirebaseTokenUpdateRequest
    ): Response<CommonDto>
}