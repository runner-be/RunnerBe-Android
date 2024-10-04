package com.applemango.runnerbe.data.dto

import android.os.Parcelable
import com.applemango.runnerbe.R
import com.applemango.runnerbe.util.dateStringToLongTime
import com.applemango.runnerbe.util.timeStringToLongTime
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Calendar

// 메인페이지(postingResult), 찜목록 조회(bookMarkList), 마이페이지(myPosting, myRunning)
// 게시글 상세(postingInfo)
@Parcelize
data class Posting(
    @SerializedName("postId") val postId: Int,
    @SerializedName("postingTime") val postingTime: String?,
    @SerializedName("postUserId") val postUserId: Int,
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("profileImageUrl") val profileImageUrl: String?,
    @SerializedName("title") val title: String,
    @SerializedName("runningTime") val runningTime: String?,
    @SerializedName("gatheringTime") val gatheringTime: String?,
    @SerializedName("gatherLongitude") val gatherLongitude: String?,
    @SerializedName("gatherLatitude") val gatherLatitude: String?,
    @SerializedName("locationInfo") val locationInfo: String?,
    @SerializedName("runningTag") val runningTag: String?,
    @SerializedName("age") val age: String,
    @SerializedName("DISTANCE") val DISTANCE: String?,
    @SerializedName("gender") val gender: String?,
    // N: 마감X, Y: 마감O
    @SerializedName("whetherEnd") val whetherEnd: String?,
    @SerializedName("job") val job: String?,
    @SerializedName("peopleNum") val peopleNum: Int,
    @SerializedName("contents") val contents: String?,
    @SerializedName("userId") val userId: Int?,
    // 0이면 찜X, 1이면 찜O
    @SerializedName("bookMark") var bookMark: Int?,
    @SerializedName("attendance") val attendance: Int?,
    // 출석처리 여부 Y: 반장이 출석체크, N: 반장이 출석체크X
    @SerializedName("whetherCheck") val whetherCheck: String?,
    @SerializedName("profileUrlList") val profileUrlList: List<ProfileUrlList>?,
    @SerializedName("runnerList") val runnerList: List<UserInfo>?,
    @SerializedName("whetherPostUser") val whetherPostUser: String?,
    @SerializedName("pace") val pace: String?,
    @SerializedName("afterParty") val afterParty: Int?
) : Parcelable {
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
        return this.whetherCheck == "Y" && this.attendance == 1
    }

    fun isWhetherEnded(): Boolean = whetherEnd == "Y"

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