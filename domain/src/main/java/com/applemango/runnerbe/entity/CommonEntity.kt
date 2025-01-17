package com.applemango.runnerbe.entity

open class CommonEntity(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String? = "Error",
)