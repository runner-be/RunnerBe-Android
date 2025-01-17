package com.applemango.runnerbe.entity

data class MyPageEntity(
    val userInfo: UserEntity?,
    val myPosting: List<PostingEntity>,
    val myRunning: List<PostingEntity>
)
