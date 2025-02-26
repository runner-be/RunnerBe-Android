package com.applemango.presentation.ui.model

data class PostingDetailModel(
    val postList : List<PostingModel>,
    val runnerInfo : List<UserModel>?,
    val waitingRunnerInfo : List<UserModel>?,
    val roomId: Int,
    val gatheringId: Int
)
