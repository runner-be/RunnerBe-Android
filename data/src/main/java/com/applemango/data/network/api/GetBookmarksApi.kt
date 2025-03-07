package com.applemango.data.network.api

import com.applemango.data.dto.BookmarksDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetBookmarksApi {
    @GET("/users/{userId}/bookmarks/v2")
    suspend fun getBookmarks(
        @Path("userId") userId: Int
    ) : Response<BookmarksDto>
}