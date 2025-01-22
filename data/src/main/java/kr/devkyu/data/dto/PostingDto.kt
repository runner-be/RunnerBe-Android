package kr.devkyu.data.dto

import com.squareup.moshi.Json
import java.time.ZonedDateTime

data class ProfileUrl(
    val userId: Int,
    val profileImageUrl: String?
)

// 메인페이지(postingResult), 찜목록 조회(bookMarkList), 마이페이지(myPosting, myRunning)
// 게시글 상세(postingInfo)
data class PostingDto(
    @Json(name = "postId") val postId: Int,
    @Json(name = "postingTime") val postingTime: String?,
    @Json(name = "postUserId") val postUserId: Int,
    @Json(name = "nickName") val nickName: String?,
    @Json(name = "profileImageUrl") val profileImageUrl: String?,
    @Json(name = "title") val title: String,
    @Json(name = "runningTime") val runningTime: String?,
    @Json(name = "gatheringTime") val gatheringTime: ZonedDateTime?,
    @Json(name = "gatherLongitude") val gatherLongitude: String?,
    @Json(name = "gatherLatitude") val gatherLatitude: String?,
    /**
     * placeName : 장소 이름
     * placeAddress : 장소 주소
     * planeExplain : 장소 설명
     */
    @Json(name = "placeName") val placeName: String?,
    @Json(name = "placeAddress") val placeAddress: String?,
    @Json(name = "placeExplain") val placeExplain: String?,
    @Json(name = "runningTag") val runningTag: String?,
    @Json(name = "age") val age: String,
    @Json(name = "DISTANCE") val distance: String?,
    @Json(name = "gender") val gender: String?,
    // N: 마감X, Y: 마감O, D: 비노출(시간이 지나서 DB 상에서 삭제됨)
    @Json(name = "whetherEnd") val whetherEnd: String?,
    @Json(name = "job") val job: String?,
    @Json(name = "peopleNum") val peopleNum: Int,
    @Json(name = "contents") val contents: String?,
    @Json(name = "userId") val userId: Int?,
    @Json(name = "logId") val logId: Int?,
    @Json(name = "gatheringId") val gatheringId: Int?,
    // 0이면 찜X, 1이면 찜O
    @Json(name = "bookMark") var bookMark: Int?,
    @Json(name = "attendance") val attendance: Int?,
    // 출석처리 여부 Y: 반장이 출석체크, N: 반장이 출석체크X
    @Json(name = "whetherCheck") val whetherCheck: String?,
    @Json(name = "whetherAccept") val whetherAccept: String?,
    @Json(name = "profileUrlList") val profileUrlList: List<kr.devkyu.data.dto.ProfileUrl>?,
    @Json(name = "runnerList") val runnerList: List<kr.devkyu.data.dto.UserDto>?,
    @Json(name = "whetherPostUser") val whetherPostUser: String?,
    @Json(name = "pace") val pace: String?,
    @Json(name = "afterParty") val afterParty: Int?,
    // 모임 종료(러닝 끝난 후)부터 3시간 지났는지 여부
    @Json(name = "attendTimeOver") val attendTimeOver: String?
)