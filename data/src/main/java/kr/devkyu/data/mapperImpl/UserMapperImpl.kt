package kr.devkyu.data.mapperImpl

import com.applemango.runnerbe.data.dto.UserDto
import kr.devkyu.data.mapper.UserMapper
import com.applemango.runnerbe.entity.UserEntity
import javax.inject.Inject

class UserMapperImpl @Inject constructor(): UserMapper {
    override fun mapToDomain(input: UserDto): UserEntity {
        return UserEntity(
            userId = input.userId,
            nickname = input.nickName,
            gender = input.gender,
            age = input.age,
            diligence = input.diligence,
            job = input.job,
            pace = input.pace,
            profileImageUrl = input.profileImageUrl,
            pushOn = input.pushOn,
            whetherCheck = input.whetherCheck,
            attendance = input.attendance,
            attendanceState = input.attendanceState,
            whetherAccept = input.whetherAccept,
            nameChanged = input.nameChanged,
            jobChangePossible = input.jobChangePossible
        )
    }

    override fun mapToData(input: UserEntity): UserDto {
        return UserDto(
            userId = input.userId,
            nickName = input.nickname,
            gender = input.gender,
            age = input.age,
            diligence = input.diligence,
            job = input.job,
            pace = input.pace,
            profileImageUrl = input.profileImageUrl,
            pushOn = input.pushOn,
            whetherCheck = input.whetherCheck,
            attendance = input.attendance,
            attendanceState = input.attendanceState,
            whetherAccept = input.whetherAccept,
            nameChanged = input.nameChanged,
            jobChangePossible = input.jobChangePossible
        )
    }
}