package com.applemango.data.mapperImpl

import com.applemango.data.dto.NewUserDto
import com.applemango.data.dto.Register
import com.applemango.data.mapper.NewUserMapper
import com.applemango.domain.entity.NewUserEntity
import javax.inject.Inject

class NewUserMapperImpl @Inject constructor():
    NewUserMapper {
    override fun mapToData(input: NewUserEntity): NewUserDto {
        return NewUserDto(
            result = Register(
                input.insertedUserId,
                input.token
            )
        )
    }

    override fun mapToDomain(input: NewUserDto): NewUserEntity {
        return NewUserEntity(
            input.result.insertedUserId,
            input.result.token,
        )
    }
}