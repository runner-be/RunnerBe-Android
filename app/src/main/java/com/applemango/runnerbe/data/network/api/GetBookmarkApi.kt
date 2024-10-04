package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.GetBookmarkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetBookmarkApi {
    @GET("/users/{userId}/bookmarks/v2")
    suspend fun postBookmark(
        @Path("userId") userId: Int
    ) : Response<GetBookmarkResponse>
}