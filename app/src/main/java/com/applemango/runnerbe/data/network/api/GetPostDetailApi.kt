package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.GetPostDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPostDetailApi {

    @GET("/postings/v2/{postId}/{userId}")
    suspend fun getPostDetail(@Path("postId") postId : Int, @Path("userId") userId: Int): Response<GetPostDetailResponse>
}