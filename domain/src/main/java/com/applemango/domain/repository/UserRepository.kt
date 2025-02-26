package com.applemango.domain.repository

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.MyPageEntity
import com.applemango.domain.entity.OtherUserEntity
import com.applemango.domain.entity.NewUserEntity
import com.applemango.domain.entity.SocialLoginEntity

interface UserRepository {

//    fun getCachedJwtToken(): String?
    suspend fun logout()
    suspend fun getUserId(): Int
    suspend fun getJwtToken(): String?
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