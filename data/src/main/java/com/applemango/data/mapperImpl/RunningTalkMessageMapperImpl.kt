package com.applemango.data.mapperImpl

import com.applemango.data.dto.RunningTalkMessageDto
import com.applemango.data.mapper.RunningTalkMessageMapper
import com.applemango.domain.entity.RunningTalkMessageEntity
import javax.inject.Inject

class RunningTalkMessageMapperImpl @Inject constructor():
    RunningTalkMessageMapper {
    override fun mapToData(input: RunningTalkMessageEntity): RunningTalkMessageDto {
        return RunningTalkMessageDto(
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

    override fun mapToDomain(input: RunningTalkMessageDto): RunningTalkMessageEntity {
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
}