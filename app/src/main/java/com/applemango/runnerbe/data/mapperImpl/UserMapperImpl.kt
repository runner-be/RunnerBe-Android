package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.User
import com.applemango.runnerbe.data.dto.new.UserDto
import com.applemango.runnerbe.data.mapper.UserMapper
import com.applemango.runnerbe.entity.UserEntity

class UserMapperImpl: UserMapper {
    override fun mapToDomain(input: UserDto): UserEntity {
        val user = input.result
        return UserEntity(
            userId = user.userId,
            nickName = user.nickName,
            gender = user.gender,
            age = user.age,
            diligence = user.diligence,
            job = user.job,
            pace = user.pace,
            profileImageUrl = user.profileImageUrl,
            pushOn = user.pushOn,
            whetherCheck = user.whetherCheck,
            attendance = user.attendance,
            attendanceState = user.attendanceState,
            whetherAccept = user.whetherAccept,
            nameChanged = user.nameChanged,
            jobChangePossible = user.jobChangePossible
        )
    }

    override fun mapToData(input: UserEntity): UserDto {
        return UserDto(
            result = User(
                userId = input.userId,
                nickName = input.nickName,
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
        )
    }
}