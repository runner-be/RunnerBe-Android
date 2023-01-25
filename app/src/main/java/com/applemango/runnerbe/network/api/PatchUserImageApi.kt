package com.applemango.runnerbe.network.api

import com.applemango.runnerbe.network.request.PatchUserImgRequest
import com.applemango.runnerbe.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchUserImageApi {
    @PATCH("/users/{userId}/profileImage")
    suspend fun patchUserImg(@Path("userId") userId: Int, @Body request: PatchUserImgRequest) : Response<BaseResponse>
}