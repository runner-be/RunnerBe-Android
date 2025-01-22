package kr.devkyu.data.mapperImpl

import kr.devkyu.data.mapper.SocialLoginMapper
import com.applemango.runnerbe.entity.SocialLoginEntity
import kr.devkyu.data.dto.SocialLoginDto
import javax.inject.Inject

class SocialLoginMapperImpl @Inject constructor():
    SocialLoginMapper {
    override fun mapToData(input: SocialLoginEntity): SocialLoginDto {
        return SocialLoginDto(
            result = input.login
        )
    }

    override fun mapToDomain(input: SocialLoginDto): SocialLoginEntity {
        return SocialLoginEntity(
            login = input.result
        )
    }
}