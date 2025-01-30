package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.MyPageEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity

interface UserRepository {

    suspend fun logout()
    suspend fun getUserId(): Int
    suspend fun getUserPace(): String
    suspend fun getDeviceToken(): String?
    suspend fun getUuid(): String?
    suspend fun updateUserPace(pace: String)
    suspend fun updateJwtToken(jwtToken: String)
    suspend fun updateUserId(userId: Int)
    suspend fun updateUuid(uuid: String)
    suspend fun updateLoginType(loginType: Int)

    suspend fun withdrawalUser(secretKey: String) : CommonEntity
    suspend fun nicknameChange(nickname : String) : CommonEntity
    suspend fun jobChange(job : String) : CommonEntity
    suspend fun patchUserImage(imageUrl : String?) : CommonEntity
    suspend fun bookMarkStatusChange(postId : Int, whetherAdd : String) : CommonEntity
    suspend fun patchUserPaceRegist(pace:String): CommonEntity

    suspend fun kakaoLogin(accessToken: String): SocialLoginEntity
    suspend fun naverLogin(accessToken: String): SocialLoginEntity
    suspend fun joinUser(
        uuid : String,
        nickName : String? = null,
        birthday: Int,
        genderTag : String,
        jobTag : String,
        deviceToken : String) : NewUserEntity
    suspend fun getUserData() : MyPageEntity
    suspend fun getOtherUserProfile(targetUserId: Int): OtherUserEntity
}