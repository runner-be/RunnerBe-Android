package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.PatchUserImgRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchUserImageApi {
    @PATCH("/users/{userId}/profileImage")
    suspend fun patchUserImg(
        @Path("userId") userId: Int,
        @Body request: PatchUserImgRequest
    ) : Response<kr.devkyu.data.dto.CommonDto>
}