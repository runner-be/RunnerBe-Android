package com.applemango.runnerbe.data.mapper

interface BaseMapper<FROM, TO> {
    fun mapToData(input: TO): FROM
    fun mapToDomain(input: FROM): TO
}