package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.OtherUserDto
import com.applemango.runnerbe.data.mapper.OtherUserMapper
import com.applemango.runnerbe.entity.OtherUserEntity
import javax.inject.Inject

class OtherUserMapperImpl @Inject constructor(): OtherUserMapper {
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