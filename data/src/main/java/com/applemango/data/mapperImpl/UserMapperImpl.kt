package com.applemango.data.mapperImpl

import com.applemango.data.dto.UserDto
import com.applemango.data.mapper.UserMapper
import com.applemango.domain.entity.UserEntity
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