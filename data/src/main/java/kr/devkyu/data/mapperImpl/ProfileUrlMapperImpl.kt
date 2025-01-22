package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.ProfileUrl
import kr.devkyu.data.mapper.ProfileUrlMapper
import com.applemango.runnerbe.entity.ProfileUrlEntity
import javax.inject.Inject

class ProfileUrlMapperImpl @Inject constructor():
    ProfileUrlMapper {
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