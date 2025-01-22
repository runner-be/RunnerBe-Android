package kr.devkyu.data.network.api

import kr.devkyu.data.dto.PostingDetailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPostDetailApi {

    @GET("/postings/v2/{postId}/{userId}")
    suspend fun getPostDetail(
        @Path("postId") postId : Int,
        @Path("userId") userId: Int
    ): Response<PostingDetailDto>
}