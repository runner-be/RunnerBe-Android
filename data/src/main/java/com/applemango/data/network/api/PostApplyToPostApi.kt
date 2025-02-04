package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApplyToPostApi {

    @POST("runnings/request/{postId}/{userId}")
    suspend fun postApply(
        @Path("postId") postId: Int,
        @Path("userId") userId : Int
    ) :Response<CommonDto>
}