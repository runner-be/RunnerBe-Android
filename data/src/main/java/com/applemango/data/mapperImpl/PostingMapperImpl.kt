package com.applemango.data.mapperImpl

import com.applemango.data.dto.PostingDto
import com.applemango.data.mapper.PostingMapper
import com.applemango.data.mapper.ProfileUrlMapper
import com.applemango.data.mapper.UserMapper
import com.applemango.domain.entity.PostingEntity
import javax.inject.Inject

class PostingMapperImpl @Inject constructor(
    private val userMapper: UserMapper,
    private val profileUrlMapper: ProfileUrlMapper
): PostingMapper {
    override fun mapToDomain(input: PostingDto): PostingEntity {
        val runnerList = input.runnerList?.map {
            userMapper.mapToDomain(it)
        } ?: emptyList()
        val profileUrlList = input.profileUrlList?.map {
            profileUrlMapper.mapToDomain(it)
        } ?: emptyList()

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
            profileUrlList = profileUrlList,
            runnerList = runnerList,
            whetherPostUser = input.whetherPostUser,
            pace = input.pace,
            afterParty = input.afterParty,
            attendTimeOver = input.attendTimeOver
        )
    }

    override fun mapToData(input: PostingEntity): PostingDto {
        val runnerList = input.runnerList?.map {
            userMapper.mapToData(it)
        } ?: emptyList()
        val profileUrlList = input.profileUrlList?.map {
            profileUrlMapper.mapToData(it)
        } ?: emptyList()

        return PostingDto(
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
            profileUrlList = profileUrlList,
            runnerList = runnerList,
            whetherPostUser = input.whetherPostUser,
            pace = input.pace,
            afterParty = input.afterParty,
            attendTimeOver = input.attendTimeOver
        )
    }
}