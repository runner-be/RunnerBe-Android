package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate

sealed class RunningTalkUiState {
    data class MyRunningTalkUiState(
        val items: List<RunningTalkItem>,
        val createTime: String,
        val isPostWriter: Boolean
    ): RunningTalkUiState()
    data class OtherRunningTalkUiState(
        val writerName: String,
        val writerProfileImgUrl: String?,
        val items: List<RunningTalkItem>,
        val createTime: String,
        val isPostWriter: Boolean,
        var isChecked: Boolean = false,
        val isReportMode: Boolean
    ): RunningTalkUiState()
}