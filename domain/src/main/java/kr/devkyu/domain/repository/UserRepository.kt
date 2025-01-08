package kr.devkyu.domain.repository

import kr.devkyu.domain.entity.AlarmEntity
import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.entity.OtherUserEntity
import kr.devkyu.domain.entity.RegisterEntity
import kr.devkyu.domain.entity.SocialLoginEntity
import kr.devkyu.domain.entity.UserEntity
import kr.devkyu.domain.usecaseImpl.user.RegisterUserUseCase
import kr.devkyu.domain.usecaseImpl.user.RegisterUserUseCase.JoinUserParam

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