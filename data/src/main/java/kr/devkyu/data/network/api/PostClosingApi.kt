package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostClosingApi {
    @POST("postings/{postId}/closing")
    suspend fun postClose(
        @Path("postId") postId: Int
    ) : Response<CommonDto>
}