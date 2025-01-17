package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.dto.CommonDto
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseRepository {
    /**
     * Usage
     * 성공/실패 여부만 확인하는 API(@return [CommonDto])의 일괄 처리를 위한 메소드
     *
     * @author Loki
     * @throws HttpException HTTP 응답 실패
     * @throws IllegalStateException 비즈니스 로직 실패
     */
    protected inline fun <R> handleApiCall(
        apiCall: () -> Response<CommonDto>,
        mapResponse: (CommonDto) -> R
    ): R {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body is CommonDto && body.isSuccess) {
                return mapResponse(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}