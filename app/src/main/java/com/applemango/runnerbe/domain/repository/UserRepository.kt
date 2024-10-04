package com.applemango.runnerbe.domain.repository

import com.applemango.runnerbe.data.network.request.JoinUserRequest
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.data.network.response.UserDataResponse
import retrofit2.Response

interface UserRepository {

    suspend fun joinUser(request: JoinUserRequest) : CommonResponse
    suspend fun getUserData(userId : Int) : Response<UserDataResponse>
    suspend fun withdrawalUser(userId: Int, secretKey: String) : CommonResponse
    suspend fun patchAlarm(userId: Int, pushOn : Boolean) : CommonResponse
    suspend fun nicknameChange(userId: Int, nickname : String) : CommonResponse
    suspend fun jobChange(userId: Int, job : String) : CommonResponse
    suspend fun patchUserImage(imageUrl : String?) : CommonResponse
    suspend fun bookMarkStatusChange(userId: Int, postId : Int, whetherAdd : String) : CommonResponse
    suspend fun patchUserPaceRegist(userId: Int, pace:String): CommonResponse
}