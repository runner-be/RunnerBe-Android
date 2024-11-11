package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.mapper

import com.applemango.runnerbe.data.dto.Messages
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkItem
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.util.LogUtil
import com.applemango.runnerbe.util.dateStringToString
import com.applemango.runnerbe.util.timeHourAndMinute
import java.text.SimpleDateFormat
import java.util.Locale

object RunningTalkDetailMapper {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    fun parseMessagesToRunningTalkUiState(messages: List<Messages>): List<RunningTalkUiState> {
        val groupedMessages = messages.groupBy {
            Pair(dateStringToString(it.createAt, formatter), it.from.lowercase())
        }
        return groupedMessages.mapNotNull { (key, groupedMessages) ->
            val targetFrom = key.second
            val items = groupedMessages.mapNotNull { messageToRunningTalkItem(it) }

            if (items.isEmpty()) return@mapNotNull null

            val target = groupedMessages.first()
            when (targetFrom) {
                "me" -> RunningTalkUiState.MyRunningTalkUiState(
                    createTime = timeHourAndMinute(target.createAt),
                    isPostWriter = target.whetherPostUser.uppercase() == "Y",
                    items = items
                )
                "others" -> RunningTalkUiState.OtherRunningTalkUiState(
                    createTime = timeHourAndMinute(target.createAt),
                    isPostWriter = target.whetherPostUser.uppercase() == "Y",
                    isReportMode = false,
                    writerName = target.nickName,
                    writerProfileImgUrl = target.profileImageUrl,
                    items = items
                )
                else -> {
                    LogUtil.errorLog("Unexpected from value: $targetFrom")
                    null
                }
            }
        }
    }

    private fun messageToRunningTalkItem(message: Messages): RunningTalkItem? {
        return if (message.content != null) RunningTalkItem.MessageTalkItem(
            id = message.messageId, message = message.content
        ) else if (message.imageUrl != null) RunningTalkItem.ImageTalkItem(
            id = message.messageId, imgUrl = message.imageUrl
        ) else null
    }
}