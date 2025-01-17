package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.ProfileUrl
import kr.devkyu.data.mapper.ProfileUrlMapper
import com.applemango.runnerbe.entity.ProfileUrlEntity
import javax.inject.Inject

class ProfileUrlMapperImpl @Inject constructor():
    ProfileUrlMapper {
    override fun mapToDomain(input: kr.devkyu.data.dto.ProfileUrl): ProfileUrlEntity {
        return ProfileUrlEntity(
            userId = input.userId,
            profileImageUrl = input.profileImageUrl
        )
    }

    override fun mapToData(input: ProfileUrlEntity): kr.devkyu.data.dto.ProfileUrl {
        return kr.devkyu.data.dto.ProfileUrl(
            userId = input.userId,
            profileImageUrl = input.profileImageUrl
        )
    }
}