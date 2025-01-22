package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.JoinedRunnerDto
import kr.devkyu.data.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.entity.JoinedRunnerEntity
import javax.inject.Inject

class JoinedRunnerMapperImpl @Inject constructor():
    JoinedRunnerMapper {
    override fun mapToData(input: JoinedRunnerEntity): JoinedRunnerDto {
        return JoinedRunnerDto(
            userId = input.userId,
            nickname = input.nickname,
            profileImageUrl = input.profileImageUrl,
            logId = input.logId,
            isOpened = input.isOpened,
            stampCode = input.stampCode,
            isCaptain = input.isCaptain,
        )
    }

    override fun mapToDomain(input: JoinedRunnerDto): JoinedRunnerEntity {
        return JoinedRunnerEntity(
            userId = input.userId,
            nickname = input.nickname,
            profileImageUrl = input.profileImageUrl,
            logId = input.logId,
            isOpened = input.isOpened,
            stampCode = input.stampCode,
            isCaptain = input.isCaptain,
        )
    }
}