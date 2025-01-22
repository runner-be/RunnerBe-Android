package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.OtherUserDto
import kr.devkyu.data.mapper.OtherUserMapper
import com.applemango.runnerbe.entity.OtherUserEntity
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