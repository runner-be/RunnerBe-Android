package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity

interface UserRepository {
    suspend fun withdrawalUser(userId: Int, secretKey: String) : CommonEntity
    suspend fun nicknameChange(userId: Int, nickname : String) : CommonEntity
    suspend fun jobChange(userId: Int, job : String) : CommonEntity
    suspend fun patchUserImage(userId: Int, imageUrl : String?) : CommonEntity
    suspend fun bookMarkStatusChange(userId: Int, postId : Int, whetherAdd : String) : CommonEntity
    suspend fun patchUserPaceRegist(userId: Int, pace:String): CommonEntity

    suspend fun kakaoLogin(accessToken: String): SocialLoginEntity
    suspend fun naverLogin(accessToken: String): SocialLoginEntity
    suspend fun joinUser(
        uuid : String,
        nickName : String? = null,
        birthday: Int,
        genderTag : String,
        jobTag : String,
        deviceToken : String) : NewUserEntity
    suspend fun getUserData(userId : Int) : UserEntity
    suspend fun getOtherUserProfile(targetUserId: Int): OtherUserEntity
}