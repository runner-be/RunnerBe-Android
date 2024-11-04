package com.applemango.runnerbe.data.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeConverter : TypeAdapter<ZonedDateTime?>() {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    override fun write(out: JsonWriter, value: ZonedDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.format(formatter))
        }
    }

    override fun read(input: JsonReader): ZonedDateTime? {
        return if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            val date = input.nextString()
            ZonedDateTime.parse(date, formatter)
        }
    }
}