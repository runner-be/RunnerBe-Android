package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.ProfileUrl
import com.applemango.runnerbe.data.mapper.ProfileUrlMapper
import com.applemango.runnerbe.entity.ProfileUrlEntity
import javax.inject.Inject

class ProfileUrlMapperImpl @Inject constructor(): ProfileUrlMapper {
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