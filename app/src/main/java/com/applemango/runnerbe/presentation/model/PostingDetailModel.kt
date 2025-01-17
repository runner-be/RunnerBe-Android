package com.applemango.runnerbe.presentation.model

data class PostingDetailModel(
    val postList : List<PostingModel>,
    val runnerInfo : List<UserModel>?,
    val waitingRunnerInfo : List<UserModel>?,
    val roomId: Int
)
