package com.applemango.presentation.ui.mapper

interface BaseMapper<FROM, TO> {
    fun mapToDomain(input: FROM): TO
    fun mapToPresentation(input: TO): FROM
}