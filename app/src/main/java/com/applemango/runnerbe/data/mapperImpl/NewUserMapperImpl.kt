package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.NewUserDto
import com.applemango.runnerbe.data.dto.Register
import com.applemango.runnerbe.data.mapper.NewUserMapper
import com.applemango.runnerbe.entity.NewUserEntity
import javax.inject.Inject

class NewUserMapperImpl @Inject constructor(): NewUserMapper {
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