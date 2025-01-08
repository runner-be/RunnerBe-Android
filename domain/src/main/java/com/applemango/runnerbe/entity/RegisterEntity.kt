package com.applemango.runnerbe.entity

data class RegisterEntity(
    val insertedUserId: Int,
    val token: String
): BaseEntity()
