package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import kr.devkyu.data.network.request.PatchUserPaceRegisterRequest
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