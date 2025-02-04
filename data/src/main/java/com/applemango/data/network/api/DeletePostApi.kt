package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface DeletePostApi {

    @PATCH("/postings/{postId}/{userId}/drop")
    suspend fun dropPost(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ) : Response<CommonDto>
}