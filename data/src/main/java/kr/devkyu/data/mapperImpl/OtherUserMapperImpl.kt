package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.OtherUserDto
import kr.devkyu.data.mapper.OtherUserMapper
import com.applemango.runnerbe.entity.OtherUserEntity
import javax.inject.Inject

class OtherUserMapperImpl @Inject constructor():
    OtherUserMapper {
    override fun mapToData(input: OtherUserEntity): kr.devkyu.data.dto.OtherUserDto {
        return kr.devkyu.data.dto.OtherUserDto(
            result = input.otherUser
        )
    }

    override fun mapToDomain(input: kr.devkyu.data.dto.OtherUserDto): OtherUserEntity {
        return OtherUserEntity(
            input.result
        )
    }
}