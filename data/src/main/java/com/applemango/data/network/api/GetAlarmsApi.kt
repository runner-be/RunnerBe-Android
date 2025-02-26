package com.applemango.data.network.api

import com.applemango.data.dto.AlarmsDto
import retrofit2.Response
import retrofit2.http.GET

interface GetAlarmsApi {

    @GET("/users/alarms")
    suspend fun getAlarms(

    ): Response<AlarmsDto>
}