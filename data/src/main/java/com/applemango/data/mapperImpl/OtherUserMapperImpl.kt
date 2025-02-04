package com.applemango.data.mapperImpl

import com.applemango.data.dto.OtherUserDto
import com.applemango.data.mapper.OtherUserMapper
import com.applemango.domain.entity.OtherUserEntity
import javax.inject.Inject

class OtherUserMapperImpl @Inject constructor():
    OtherUserMapper {
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