package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.EditNicknameRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UpdateNicknameApi {
    @PATCH("/users/{userId}/name")
    suspend fun editNickname(
        @Path("userId") userId: Int,
        @Body body: EditNicknameRequest
    ) : Response<kr.devkyu.data.dto.CommonDto>
}