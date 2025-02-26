package com.applemango.data.mapperImpl

import com.applemango.data.dto.SocialLoginDto
import com.applemango.data.mapper.SocialLoginMapper
import com.applemango.domain.entity.SocialLoginEntity
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