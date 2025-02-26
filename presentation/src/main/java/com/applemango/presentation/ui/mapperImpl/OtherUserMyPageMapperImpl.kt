package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.OtherUserEntity
import com.applemango.domain.entity.OtherUserInfo
import com.applemango.presentation.ui.mapper.OtherUserMyPageMapper
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.model.OtherUserMyPageModel
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.ProfileUrlModel
import javax.inject.Inject

class OtherUserMyPageMapperImpl @Inject constructor(
    private val userMapper: UserMapper
) : OtherUserMyPageMapper {
    override fun mapToDomain(input: OtherUserMyPageModel): OtherUserEntity {
        return OtherUserEntity(
            com.applemango.domain.entity.OtherUser(
                userInfo = OtherUserInfo(
                    userId = input.otherUser.userInfo.userId,
                    nickName = input.otherUser.userInfo.nickName,
                    gender = input.otherUser.userInfo.gender,
                    age = input.otherUser.userInfo.age,
                    diligence = input.otherUser.userInfo.diligence,
                    job = input.otherUser.userInfo.job,
                    profileImageUrl = input.otherUser.userInfo.profileImageUrl,
                    pace = input.otherUser.userInfo.pace,
                ),
                userLogInfo = input.otherUser.userLogInfo.map {
                    com.applemango.domain.entity.RunningLog(
                        logId = it.logId,
                        gatheringId = it.gatheringId,
                        runnedDate = it.runnedDate,
                        stampCode = it.stampCode,
                        isOpened = it.isOpened,
                    )
                },
                postTotalCount = input.otherUser.postTotalCount,
                userPosting = input.otherUser.userPosting.map {
                    com.applemango.domain.entity.PostingEntity(
                        postId = it.postId,
                        postingTime = it.postingTime,
                        postUserId = it.postUserId,
                        nickName = it.nickName,
                        profileImageUrl = it.profileImageUrl,
                        title = it.title,
                        runningTime = it.runningTime,
                        gatheringTime = it.gatheringTime,
                        gatherLongitude = it.gatherLongitude,
                        gatherLatitude = it.gatherLatitude,
                        placeName = it.placeName,
                        placeAddress = it.placeAddress,
                        placeExplain = it.placeExplain,
                        runningTag = it.runningTag,
                        age = it.age,
                        distance = it.distance,
                        gender = it.gender,
                        whetherEnd = it.whetherEnd,
                        job = it.job,
                        peopleNum = it.peopleNum,
                        contents = it.contents,
                        userId = it.userId,
                        logId = it.logId,
                        gatheringId = it.gatheringId,
                        bookMark = it.bookMark,
                        attendance = it.attendance,
                        whetherCheck = it.whetherCheck,
                        whetherAccept = it.whetherAccept,
                        profileUrlList = it.profileUrlList?.map { profile ->
                            com.applemango.domain.entity.ProfileUrlEntity(
                                profile.userId,
                                profile.profileImageUrl
                            )
                        },
                        runnerList = it.runnerList?.map { user ->
                            userMapper.mapToDomain(user)
                        },
                        whetherPostUser = it.whetherPostUser,
                        pace = it.pace,
                        afterParty = it.afterParty,
                        attendTimeOver = it.attendTimeOver
                    )
                }
            )
        )
    }

    override fun mapToPresentation(input: OtherUserEntity): OtherUserMyPageModel {
        return OtherUserMyPageModel(
            com.applemango.presentation.ui.model.OtherUser(
                userInfo = com.applemango.presentation.ui.model.OtherUserInfo(
                    userId = input.otherUser.userInfo.userId,
                    nickName = input.otherUser.userInfo.nickName,
                    gender = input.otherUser.userInfo.gender,
                    age = input.otherUser.userInfo.age,
                    diligence = input.otherUser.userInfo.diligence,
                    job = input.otherUser.userInfo.job,
                    profileImageUrl = input.otherUser.userInfo.profileImageUrl,
                    pace = input.otherUser.userInfo.pace,
                ),
                userLogInfo = input.otherUser.userLogInfo.map {
                    com.applemango.domain.entity.RunningLog(
                        logId = it.logId,
                        gatheringId = it.gatheringId,
                        runnedDate = it.runnedDate,
                        stampCode = it.stampCode,
                        isOpened = it.isOpened,
                    )
                },
                postTotalCount = input.otherUser.postTotalCount,
                userPosting = input.otherUser.userPosting.map {
                    PostingModel(
                        postId = it.postId,
                        postingTime = it.postingTime,
                        postUserId = it.postUserId,
                        nickName = it.nickName,
                        profileImageUrl = it.profileImageUrl,
                        title = it.title,
                        runningTime = it.runningTime,
                        gatheringTime = it.gatheringTime,
                        gatherLongitude = it.gatherLongitude,
                        gatherLatitude = it.gatherLatitude,
                        placeName = it.placeName,
                        placeAddress = it.placeAddress,
                        placeExplain = it.placeExplain,
                        runningTag = it.runningTag,
                        age = it.age,
                        distance = it.distance,
                        gender = it.gender,
                        whetherEnd = it.whetherEnd,
                        job = it.job,
                        peopleNum = it.peopleNum,
                        contents = it.contents,
                        userId = it.userId,
                        logId = it.logId,
                        gatheringId = it.gatheringId,
                        bookMark = it.bookMark,
                        attendance = it.attendance,
                        whetherCheck = it.whetherCheck,
                        whetherAccept = it.whetherAccept,
                        profileUrlList = it.profileUrlList?.map { profile ->
                            ProfileUrlModel(
                                profile.userId,
                                profile.profileImageUrl,
                            )
                        },
                        runnerList = it.runnerList?.map { user ->
                            userMapper.mapToPresentation(user)
                        },
                        whetherPostUser = it.whetherPostUser,
                        pace = it.pace,
                        afterParty = it.afterParty,
                        attendTimeOver = it.attendTimeOver
                    )
                }
            )
        )
    }
}