package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.presentation.mapper.UserMapper
import com.applemango.runnerbe.presentation.model.UserModel
import javax.inject.Inject

class UserMapperImpl @Inject constructor(): UserMapper {
    override fun mapToDomain(input: UserModel): UserEntity {
        return UserEntity(
            userId = input.userId,
            nickname = input.nickname,
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
            jobChangePossible = input.jobChangePossible,
        )
    }

    override fun mapToPresentation(input: UserEntity): UserModel {
        return UserModel(
            userId = input.userId,
            nickname = input.nickname,
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
            jobChangePossible = input.jobChangePossible,
        )
    }
}