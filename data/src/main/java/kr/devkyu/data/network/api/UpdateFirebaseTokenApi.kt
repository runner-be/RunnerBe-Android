package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import kr.devkyu.data.network.request.FirebaseTokenUpdateRequest
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