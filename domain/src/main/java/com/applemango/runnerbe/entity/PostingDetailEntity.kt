package com.applemango.runnerbe.entity

data class PostingDetailEntity(
    val postingDetail: PostingDetail
)

data class PostingDetail(
    val postList : List<PostingEntity>,
    val runnerInfo : List<UserEntity>?,
    val waitingRunnerInfo : List<UserEntity>?,
    val roomId: Int
)