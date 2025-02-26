package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.EditNicknameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UpdateNicknameApi {
    @PATCH("/users/{userId}/name")
    suspend fun editNickname(
        @Path("userId") userId: Int,
        @Body body: EditNicknameRequest
    ) : Response<CommonDto>
}