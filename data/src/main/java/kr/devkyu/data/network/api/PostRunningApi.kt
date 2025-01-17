package kr.devkyu.data.network.api

import kr.devkyu.data.dto.CommonDto
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PostRunningApi {

    @POST("postings/{userId}")
    suspend fun writingRunning(
        @Path("userId") userId: Int,
        @Body body: WriteRunningRequest
    ) : Response<kr.devkyu.data.dto.CommonDto>
}