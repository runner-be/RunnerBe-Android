package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.WithdrawalUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteUserApi {
    @DELETE("users/{userId}")
    suspend fun withdrawalUser(
        @Path("userId") userId: Int,
        @Body request: WithdrawalUserRequest
    ) : Response<kr.devkyu.data.dto.CommonDto>
}