package com.applemango.runnerbe.entity

open class BaseEntity(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String? = null,
)