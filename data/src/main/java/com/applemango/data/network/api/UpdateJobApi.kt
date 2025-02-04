package com.applemango.data.network.api

import com.applemango.data.dto.CommonDto
import com.applemango.data.network.request.EditJobRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UpdateJobApi {
    @PATCH("/users/{userId}/job")
    suspend fun editJob(
        @Path("userId") userId: Int,
        @Body body: EditJobRequest
    ) : Response<CommonDto>
}