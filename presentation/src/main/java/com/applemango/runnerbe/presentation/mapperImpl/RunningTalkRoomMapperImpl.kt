package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.RunningTalkRoomEntity
import com.applemango.runnerbe.presentation.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.presentation.model.RunningTalkRoomModel
import javax.inject.Inject

class RunningTalkRoomMapperImpl @Inject constructor(): RunningTalkRoomMapper {
    override fun mapToDomain(input: RunningTalkRoomModel): RunningTalkRoomEntity {
        return RunningTalkRoomEntity(
            roomId = input.roomId,
            title = input.title,
            repUserName = input.repUserName,
            profileImageUrl = input.profileImageUrl,
            recentMessage = input.recentMessage,
        )
    }

    override fun mapToPresentation(input: RunningTalkRoomEntity): RunningTalkRoomModel {
        return RunningTalkRoomModel(
            roomId = input.roomId,
            title = input.title,
            repUserName = input.repUserName,
            profileImageUrl = input.profileImageUrl,
            recentMessage = input.recentMessage,
        )
    }
}