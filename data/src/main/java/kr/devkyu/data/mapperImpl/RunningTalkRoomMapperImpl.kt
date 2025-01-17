package kr.devkyu.data.mapperImpl

import com.applemango.runnerbe.data.dto.RunningTalkRoomDto
import kr.devkyu.data.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.entity.RunningTalkRoomEntity
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