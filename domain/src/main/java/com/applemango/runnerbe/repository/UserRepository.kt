package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.RegisterEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.usecaseImpl.user.RegisterUserUseCase.JoinUserParam

interface UserRepository {

    suspend fun kakaoLogin(accessToken: String): SocialLoginEntity
    suspend fun naverLogin(accessToken: String): SocialLoginEntity

    suspend fun joinUser(request: JoinUserParam) : RegisterEntity
    suspend fun getUserData(userId : Int) : UserEntity
    suspend fun withdrawalUser(userId: Int, secretKey: String) : BaseEntity
    suspend fun patchAlarm(userId: Int, pushOn : Boolean) : BaseEntity
    suspend fun nicknameChange(userId: Int, nickname : String) : BaseEntity
    suspend fun jobChange(userId: Int, job : String) : BaseEntity
    suspend fun patchUserImage(imageUrl : String?) : BaseEntity
    suspend fun bookMarkStatusChange(userId: Int, postId : Int, whetherAdd : String) : BaseEntity
    suspend fun patchUserPaceRegist(userId: Int, pace:String): BaseEntity
    suspend fun getAlarms(): AlarmEntity

    suspend fun getOtherUserProfile(targetUserId: Int): OtherUserEntity
}