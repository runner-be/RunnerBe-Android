package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.ProfileUrlEntity
import com.applemango.runnerbe.presentation.mapper.PostingMapper
import com.applemango.runnerbe.presentation.mapper.UserMapper
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.ProfileUrlModel
import javax.inject.Inject

class PostingMapperImpl @Inject constructor(
    private val userMapper: UserMapper
): PostingMapper {
    override fun mapToDomain(input: PostingModel): PostingEntity {
        return PostingEntity(
            postId = input.postId,
            postingTime = input.postingTime,
            postUserId = input.postUserId,
            nickName = input.nickName,
            profileImageUrl = input.profileImageUrl,
            title = input.title,
            runningTime = input.runningTime,
            gatheringTime = input.gatheringTime,
            gatherLongitude = input.gatherLongitude,
            gatherLatitude = input.gatherLatitude,
            placeName = input.placeName,
            placeAddress = input.placeAddress,
            placeExplain = input.placeExplain,
            runningTag = input.runningTag,
            age = input.age,
            distance = input.distance,
            gender = input.gender,
            whetherEnd = input.whetherEnd,
            job = input.job,
            peopleNum = input.peopleNum,
            contents = input.contents,
            userId = input.userId,
            logId = input.logId,
            gatheringId = input.gatheringId,
            bookMark = input.bookMark,
            attendance = input.attendance,
            whetherCheck = input.whetherCheck,
            whetherAccept = input.whetherAccept,
            profileUrlList = input.profileUrlList?.map { profile ->
                ProfileUrlEntity(
                    profile.userId,
                    profile.profileImageUrl
                )
            },
            runnerList = input.runnerList?.map { user ->
                userMapper.mapToDomain(user)
            },
            whetherPostUser = input.whetherPostUser,
            pace = input.pace,
            afterParty = input.afterParty,
            attendTimeOver = input.attendTimeOver
        )
    }

    override fun mapToPresentation(input: PostingEntity): PostingModel {
        return PostingModel(
            postId = input.postId,
            postingTime = input.postingTime,
            postUserId = input.postUserId,
            nickName = input.nickName,
            profileImageUrl = input.profileImageUrl,
            title = input.title,
            runningTime = input.runningTime,
            gatheringTime = input.gatheringTime,
            gatherLongitude = input.gatherLongitude,
            gatherLatitude = input.gatherLatitude,
            placeName = input.placeName,
            placeAddress = input.placeAddress,
            placeExplain = input.placeExplain,
            runningTag = input.runningTag,
            age = input.age,
            distance = input.distance,
            gender = input.gender,
            whetherEnd = input.whetherEnd,
            job = input.job,
            peopleNum = input.peopleNum,
            contents = input.contents,
            userId = input.userId,
            logId = input.logId,
            gatheringId = input.gatheringId,
            bookMark = input.bookMark,
            attendance = input.attendance,
            whetherCheck = input.whetherCheck,
            whetherAccept = input.whetherAccept,
            profileUrlList = input.profileUrlList?.map { profile ->
                ProfileUrlModel(
                    profile.userId,
                    profile.profileImageUrl,
                )
            },
            runnerList = input.runnerList?.map { user ->
                userMapper.mapToPresentation(user)
            },
            whetherPostUser = input.whetherPostUser,
            pace = input.pace,
            afterParty = input.afterParty,
            attendTimeOver = input.attendTimeOver
        )
    }
}