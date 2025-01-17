package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.SocialLoginDto
import com.applemango.runnerbe.data.mapper.SocialLoginMapper
import com.applemango.runnerbe.entity.SocialLoginEntity
import javax.inject.Inject

class SocialLoginMapperImpl @Inject constructor(): SocialLoginMapper {
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