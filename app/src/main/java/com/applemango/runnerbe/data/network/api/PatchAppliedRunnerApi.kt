package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchAppliedRunnerApi {

    @PATCH("runnings/request/{postId}/handling/{applicantId}/{whetherAccept}")
    suspend fun whetherAccept(
        @Path("postId") postId: Int,
        @Path("applicantId") applicantId: Int,
        @Path("whetherAccept") whetherAccept: String
    ): Response<BaseResponse>
}