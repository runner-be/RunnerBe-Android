package com.applemango.runnerbe.entity

import java.time.ZonedDateTime

data class PostingEntity(
    val postId: Int,
    val postingTime: String?,
    val postUserId: Int,
    val nickName: String?,
    val profileImageUrl: String?,
    val title: String,
    val runningTime: String?,
    val gatheringTime: ZonedDateTime?,
    val gatherLongitude: String?,
    val gatherLatitude: String?,
    /**
     * placeName : 장소 이름
     * placeAddress : 장소 주소
     * planeExplain : 장소 설명
     */
    val placeName: String?,
    val placeAddress: String?,
    val placeExplain: String?,
    val runningTag: String?,
    val age: String,
    val distance: String?,
    val gender: String?,
    val whetherEnd: String?, // N: 마감X, Y: 마감O, D: 비노출(시간이 지나서 DB 상에서 삭제됨)
    val job: String?,
    val peopleNum: Int,
    val contents: String?,
    val userId: Int?,
    val logId: Int?,
    val gatheringId: Int?,
    var bookMark: Int?, // 0이면 찜X, 1이면 찜O
    val attendance: Int?,
    val whetherCheck: String?, // 출석처리 여부 Y: 반장이 출석체크, N: 반장이 출석체크X
    val whetherAccept: String?,
    val profileUrlList: List<ProfileUrlEntity>?,
    val runnerList: List<UserEntity>?,
    val whetherPostUser: String?,
    val pace: String?,
    val afterParty: Int?,
    // 모임 종료(러닝 끝난 후)부터 3시간 지났는지 여부
    val attendTimeOver: String?
): BaseEntity()
