package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.request.AttendanceAccessionRequest
import com.applemango.runnerbe.data.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AttendanceAccessionApi {

    @PATCH("runnings/{postId}/attend")
    suspend fun attendanceAccession(
        @Path("postId") postId: Int,
        @Body request: AttendanceAccessionRequest
    ): Response<BaseResponse>
}