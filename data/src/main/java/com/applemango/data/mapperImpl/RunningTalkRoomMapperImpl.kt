package com.applemango.data.mapperImpl

import com.applemango.data.dto.RunningTalkRoomDto
import com.applemango.data.mapper.RunningTalkRoomMapper
import com.applemango.domain.entity.RunningTalkRoomEntity
import javax.inject.Inject

class RunningTalkRoomMapperImpl @Inject constructor():
    RunningTalkRoomMapper {
    override fun mapToData(input: RunningTalkRoomEntity): RunningTalkRoomDto {
        return RunningTalkRoomDto(
            roomId = input.roomId,
            title = input.title,
            repUserName = input.repUserName,
            profileImageUrl = input.profileImageUrl,
            recentMessage = input.recentMessage,
        )
    }

    override fun mapToDomain(input: RunningTalkRoomDto): RunningTalkRoomEntity {
        return RunningTalkRoomEntity(
            roomId = input.roomId,
            title = input.title,
            repUserName = input.repUserName,
            profileImageUrl = input.profileImageUrl,
            recentMessage = input.recentMessage,
        )
    }
}