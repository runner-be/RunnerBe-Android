package com.applemango.presentation.ui.screen.fragment.chat.detail.mapper

import com.applemango.presentation.ui.model.RunningTalkMessageModel
import com.applemango.presentation.ui.screen.fragment.chat.detail.uistate.RunningTalkItem
import com.applemango.presentation.ui.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.presentation.util.dateStringToString
import com.applemango.presentation.util.timeHourAndMinute
import java.text.SimpleDateFormat
import java.util.Locale

object RunningTalkDetailMapper {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)

    fun parseMessagesToRunningTalkUiState(messages: List<RunningTalkMessageModel>): List<RunningTalkUiState> {
        val groupedMessages = messages.groupBy {
            val parsedDateString = dateStringToString(it.createAt, formatter)
            val messageFrom = it.from.lowercase()
            Pair(parsedDateString, messageFrom) // ex) Pair("2024-11-18 14:00", "me")
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
                    null
                }
            }
        }
    }

    private fun messageToRunningTalkItem(message: RunningTalkMessageModel): RunningTalkItem? {
        return if (message.content != null) RunningTalkItem.MessageTalkItem(
            id = message.messageId, message = message.content
        ) else if (message.imageUrl != null) RunningTalkItem.ImageTalkItem(
            id = message.messageId, imgUrl = message.imageUrl
        ) else null
    }
}