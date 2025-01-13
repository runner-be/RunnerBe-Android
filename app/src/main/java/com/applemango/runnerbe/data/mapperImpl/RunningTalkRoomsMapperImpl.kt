package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.RunningTalkRoomsDto
import com.applemango.runnerbe.data.mapper.RunningTalkRoomsMapper
import com.applemango.runnerbe.entity.RunningTalkRoomsEntity

class RunningTalkRoomsMapperImpl: RunningTalkRoomsMapper {
    override fun mapToData(input: RunningTalkRoomsEntity): RunningTalkRoomsDto {
        return RunningTalkRoomsDto(
            result = input.result
        )
    }

    override fun mapToDomain(input: RunningTalkRoomsDto): RunningTalkRoomsEntity {
        return RunningTalkRoomsEntity(
            result = input.result
        )
    }
}