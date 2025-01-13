package com.applemango.runnerbe.data.dto.new

import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.util.dateStringToLongTime
import com.applemango.runnerbe.util.timeStringToLongTime
import com.squareup.moshi.Json
import java.time.ZonedDateTime
import java.util.Calendar

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
    @Json(name = "profileUrlList") val profileUrlList: List<ProfileUrl>?,
    @Json(name = "runnerList") val runnerList: List<UserDto>?,
    @Json(name = "whetherPostUser") val whetherPostUser: String?,
    @Json(name = "pace") val pace: String?,
    @Json(name = "afterParty") val afterParty: Int?,
    // 모임 종료(러닝 끝난 후)부터 3시간 지났는지 여부
    @Json(name = "attendTimeOver") val attendTimeOver: String?
) {

    private fun getMyUserId(): Int = RunnerBeApplication.mTokenPreference.getUserId()

    fun isRunningCaptain() = (getMyUserId() == this.postUserId)

    // 모임 종료 후 3시간이 지났다면 출석 확인, 지나지 않았다면 출석 관리
    fun attendanceCheck(): Boolean {
        return attendTimeOver == "Y"
    }
    fun bookmarkCheck(): Boolean {
        return this.bookMark == 1
    }

    fun attentionString(): String {
        return if(this.whetherCheck == "Y") {
            if(this.attendance == 1)"출석을 완료했어요 \uD83D\uDE0E"
            else "불참했어요 \uD83D\uDE2D"
        } else "리더의 체크를 기다리고 있어요 \uD83E\uDD7A"
    }

    fun attentionCheck(): Boolean {
//        return this.whetherCheck == "Y" && this.attendance == 1 -> 의도가 뭔지?
        return this.whetherCheck == "Y"
    }

    fun isWhetherEnded(): Boolean = whetherEnd != "N"

    fun groupString(): String {
        return try {
            val min = this.age.split("-")[0].toInt()
            if(min < 20) return genderString()
            else genderString()+" "+this.age
        }catch (e: java.lang.Exception) {
            e.printStackTrace()
            genderString()+" "+this.age
        }
    }

    fun genderString() : String {
        return if(this.gender =="전체") this.gender
        else "${this.gender}만"
    }

    //이거 데이터바인딩 시에 너무 자주 도는 이유를 찾자
    //리사이클러뷰 뷰홀더에 데이터바인딩을 data class의 메소드로 하는 경우
    //리사이클러뷰의 이동이 없는 경우 무한으로 메소드가 동작, why?
    fun myPostAttendanceMessageResource() : Int {
        val now = Calendar.getInstance().time
        val threeHour = 3 * 60 * 60 * 1000
        if(gatheringTime != null && runningTime != null) {
            val startTime = dateStringToLongTime(gatheringTime)
            val runningTime = timeStringToLongTime(this.runningTime)
            return if(now.time - startTime > 0) {
                if(startTime + threeHour + runningTime - now.time > 0) R.string.attendance_managing //여기 소요시간도 추가할 것
                else R.string.attendance_see
            } else R.string.msg_attendance_waiting
        }
        return R.string.msg_attendance_waiting
    }
}