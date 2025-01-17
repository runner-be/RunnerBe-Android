package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.dto.AlarmsDto
import retrofit2.Response
import retrofit2.http.GET

interface GetAlarmsApi {

    @GET("/users/alarms")
    fun getAlarms(

    ): Response<AlarmsDto>
}