package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.WithdrawalUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.Path

interface DeleteUserApi {
    @HTTP(method = "DELETE", path = "users/{userId}", hasBody = true)
    suspend fun withdrawalUser(
        @Path("userId") userId: Int,
        @Body request: WithdrawalUserRequest
    ) : Response<CommonDto>
}