package com.applemango.domain.entity

data class PostingDetailEntity(
    val postList : List<PostingEntity>,
    val runnerInfo : List<UserEntity>?,
    val waitingRunnerInfo : List<UserEntity>?,
    val roomId: Int
)