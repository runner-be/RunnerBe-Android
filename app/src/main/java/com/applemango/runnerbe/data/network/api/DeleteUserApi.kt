package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.WithdrawalUserRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteUserApi {
    @DELETE("users/{userId}")
    suspend fun withdrawalUser(
        @Path("userId") userId: Int,
        @Body request: WithdrawalUserRequest
    ) : Response<BaseResponse>
}