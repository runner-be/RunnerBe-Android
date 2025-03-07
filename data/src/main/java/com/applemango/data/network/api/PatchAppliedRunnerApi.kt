package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchAppliedRunnerApi {

    @PATCH("runnings/request/{postId}/handling/{applicantId}/{whetherAccept}")
    suspend fun whetherAccept(
        @Path("postId") postId: Int,
        @Path("applicantId") applicantId: Int,
        @Path("whetherAccept") whetherAccept: String
    ): Response<CommonDto>
}