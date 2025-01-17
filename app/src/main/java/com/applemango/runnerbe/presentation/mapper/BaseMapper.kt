package com.applemango.runnerbe.presentation.mapper

interface BaseMapper<FROM, TO> {
    fun mapToDomain(input: FROM): TO
    fun mapToPresentation(input: TO): FROM
}