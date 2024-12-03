package com.applemango.runnerbe.data.network.api

import com.applemango.runnerbe.data.network.response.GetNotificationsResponse
import retrofit2.Response
import retrofit2.http.GET

interface GetAlarmsApi {

    @GET("https://www.runnerbe.shop/users/alarms")
    suspend fun getNotifications(

    ): Response<GetNotificationsResponse>
}