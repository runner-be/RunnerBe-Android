package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.ProfileUrl
import com.applemango.runnerbe.data.mapper.ProfileUrlMapper
import com.applemango.runnerbe.entity.ProfileUrlEntity

class ProfileUrlMapperImpl: ProfileUrlMapper {
    override fun mapToDomain(input: ProfileUrl): ProfileUrlEntity {
        return ProfileUrlEntity(
            userId = input.userId,
            profileImageUrl = input.profileImageUrl
        )
    }

    override fun mapToData(input: ProfileUrlEntity): ProfileUrl {
        return ProfileUrl(
            userId = input.userId,
            profileImageUrl = input.profileImageUrl
        )
    }
}