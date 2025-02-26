package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.RunningTalkMessageEntity
import com.applemango.presentation.ui.mapper.RunningTalkMessageMapper
import com.applemango.presentation.ui.model.RunningTalkMessageModel
import javax.inject.Inject

class RunningTalkMessageMapperImpl @Inject constructor(): RunningTalkMessageMapper {
    override fun mapToDomain(input: RunningTalkMessageModel): RunningTalkMessageEntity {
        return RunningTalkMessageEntity(
            messageId = input.messageId,
            content = input.content,
            imageUrl = input.imageUrl,
            createAt = input.createAt,
            userId = input.userId,
            nickName = input.nickName,
            profileImageUrl = input.profileImageUrl,
            from = input.from,
            whetherPostUser = input.whetherPostUser,
            isChecked = input.isChecked,
        )
    }

    override fun mapToPresentation(input: RunningTalkMessageEntity): RunningTalkMessageModel {
        return RunningTalkMessageModel(
            messageId = input.messageId,
            content = input.content,
            imageUrl = input.imageUrl,
            createAt = input.createAt,
            userId = input.userId,
            nickName = input.nickName,
            profileImageUrl = input.profileImageUrl,
            from = input.from,
            whetherPostUser = input.whetherPostUser,
            isChecked = input.isChecked,
        )
    }
}