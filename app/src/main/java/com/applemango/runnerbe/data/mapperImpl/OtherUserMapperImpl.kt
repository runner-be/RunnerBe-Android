package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.OtherUserDto
import com.applemango.runnerbe.data.mapper.OtherUserMapper
import com.applemango.runnerbe.entity.OtherUserEntity

class OtherUserMapperImpl: OtherUserMapper {
    override fun mapToData(input: OtherUserEntity): OtherUserDto {
        return OtherUserDto(
            result = input.otherUser
        )
    }

    override fun mapToDomain(input: OtherUserDto): OtherUserEntity {
        return OtherUserEntity(
            input.result
        )
    }
}