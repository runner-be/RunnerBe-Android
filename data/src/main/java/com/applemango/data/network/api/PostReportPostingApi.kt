package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface PostReportPostingApi {

    @POST("postings/{postId}/report/{userId}")
    suspend fun reportPosting(
        @Path("postId") postId: Int,
        @Path("userId") userId: Int
    ): Response<CommonDto>
}