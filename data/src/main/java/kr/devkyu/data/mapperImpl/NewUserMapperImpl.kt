package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.NewUserDto
import kr.devkyu.data.dto.Register
import kr.devkyu.data.mapper.NewUserMapper
import com.applemango.runnerbe.entity.NewUserEntity
import javax.inject.Inject

class NewUserMapperImpl @Inject constructor():
    NewUserMapper {
    override fun mapToData(input: NewUserEntity): kr.devkyu.data.dto.NewUserDto {
        return kr.devkyu.data.dto.NewUserDto(
            result = kr.devkyu.data.dto.Register(
                input.insertedUserId,
                input.token
            )
        )
    }

    override fun mapToDomain(input: kr.devkyu.data.dto.NewUserDto): NewUserEntity {
        return NewUserEntity(
            input.result.insertedUserId,
            input.result.token,
        )
    }
}