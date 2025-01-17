package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApplyToPostApi {

    @POST("runnings/request/{postId}/{userId}")
    suspend fun postApply(
        @Path("postId") postId: Int,
        @Path("userId") userId : Int
    ) :Response<kr.devkyu.data.dto.CommonDto>
}