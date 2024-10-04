package com.applemango.runnerbe.data.network.request

data class PatchStampRequest(
    val targetId: String,
    val stampCode: String,
)
