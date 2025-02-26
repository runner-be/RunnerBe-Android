package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.AttendanceAccessionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PatchJoinedRunnerAttendanceApi {

    @PATCH("runnings/{postId}/attend")
    suspend fun attendanceAccession(
        @Path("postId") postId: Int,
        @Body request: AttendanceAccessionRequest
    ): Response<CommonDto>
}