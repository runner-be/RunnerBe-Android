package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookMarkStatusChangeApi {

    @POST("users/{userId}/bookmarks/{whetherAdd}")
    suspend fun bookMarkStatusChange(
        @Path("userId") userId: Int,
        @Path("whetherAdd") whetherAdd: String,
        @Query("postId") postId: Int
    ): Response<BaseResponse>
}