package kr.devkyu.data.mapperImpl

import kr.devkyu.data.mapper.RunningTalkMessageMapper
import com.applemango.runnerbe.entity.RunningTalkMessageEntity
import kr.devkyu.data.dto.RunningTalkMessageDto
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