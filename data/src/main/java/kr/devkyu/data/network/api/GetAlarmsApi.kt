package kr.devkyu.data.network.api

import kr.devkyu.data.dto.AlarmsDto
import retrofit2.Response
import retrofit2.http.GET

interface GetAlarmsApi {

    @GET("/users/alarms")
    fun getAlarms(

    ): Response<kr.devkyu.data.dto.AlarmsDto>
}