package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.RunningTalkMessagesDto
import com.applemango.runnerbe.data.mapper.RunningTalkMessagesMapper
import com.applemango.runnerbe.entity.RunningTalkMessagesEntity

class RunningTalkMessagesMapperImpl: RunningTalkMessagesMapper {
    override fun mapToData(input: RunningTalkMessagesEntity): RunningTalkMessagesDto {
        return RunningTalkMessagesDto(
            roomInfo = input.roomInfo,
            messages = input.messages,
        )
    }

    override fun mapToDomain(input: RunningTalkMessagesDto): RunningTalkMessagesEntity {
        return RunningTalkMessagesEntity(
            roomInfo = input.roomInfo,
            messages = input.messages,
        )
    }
}