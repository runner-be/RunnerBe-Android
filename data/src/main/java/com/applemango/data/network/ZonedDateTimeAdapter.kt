package com.applemango.data.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeAdapter {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @FromJson
    fun fromJson(
        json: String
    ): ZonedDateTime {
        return ZonedDateTime.parse(json, formatter)
    }

    @ToJson
    fun toJson(value: ZonedDateTime): String {
        return value.format(formatter)
    }
}