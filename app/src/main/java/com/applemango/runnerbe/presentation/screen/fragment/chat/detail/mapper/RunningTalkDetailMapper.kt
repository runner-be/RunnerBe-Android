package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.mapper

import com.applemango.runnerbe.data.dto.Messages
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkItem
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.uistate.RunningTalkUiState
import com.applemango.runnerbe.util.dateStringToString
import com.applemango.runnerbe.util.timeHourAndMinute
import java.text.SimpleDateFormat
import java.util.Locale

object RunningTalkDetailMapper {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
    fun messagesToRunningTalkUiState(messages: List<Messages>): List<RunningTalkUiState> {
        val result: ArrayList<RunningTalkUiState> = arrayListOf()
        var index = 0
        while (index < messages.size) {
            val target = messages[index]
            val items: ArrayList<RunningTalkItem> = arrayListOf()
            messageToRunningTalkItem(target)?.let { addedItem ->
                items.add(addedItem)
            }
            val targetDate = dateStringToString(target.createAt, formatter)
            index++
            if (items.isNotEmpty()) {
                while (index < messages.size) {
                    val item = messages[index]
                    val itemDate = dateStringToString(item.createAt, formatter)
                    if(targetDate == itemDate) {
                        messageToRunningTalkItem(item)?.let {
                            items.add(it)
                        }
                        index++
                    } else break
                }
                result.add(if(target.from.lowercase() == "me") RunningTalkUiState.MyRunningTalkUiState(
                    createTime = timeHourAndMinute(target.createAt),
                    isPostWriter = target.whetherPostUser.uppercase() == "Y",
                    items = items
                )
                else RunningTalkUiState.OtherRunningTalkUiState(
                    createTime = timeHourAndMinute(target.createAt),
                    isPostWriter = target.whetherPostUser.uppercase() == "Y",
                    isReportMode = false,
                    writerName = target.nickName,
                    writerProfileImgUrl = target.profileImageUrl,
                    items = items
                ))
            }
        }
        return result
    }

    private fun messageToRunningTalkItem(message: Messages): RunningTalkItem? {
        return if (message.content != null) RunningTalkItem.MessageTalkItem(
            id = message.messageId, message = message.content
        ) else if (message.imageUrl != null) RunningTalkItem.ImageTalkItem(
            id = message.messageId, imgUrl = message.imageUrl
        ) else null
    }
}