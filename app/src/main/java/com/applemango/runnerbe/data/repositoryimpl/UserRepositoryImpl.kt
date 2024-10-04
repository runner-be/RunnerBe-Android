package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.data.network.api.*
import com.applemango.runnerbe.data.network.request.*
import com.applemango.runnerbe.domain.repository.UserRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import com.applemango.runnerbe.data.network.response.UserDataResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val registerUserApi: RegisterUserApi,
    private val getUserDataApi: GetUserDataApi,
    private val withdrawalApi: WithdrawalApi,
    private val patchAlarmApi: PatchAlarmApi,
    private val nicknameChangeApi: NicknameChangeApi,
    private val jobChangeApi: EditJobApi,
    private val patchUserImageApi: PatchUserImageApi,
    private val bookMarkStatusChangeApi: BookMarkStatusChangeApi,
    private val patchUserPaceApi: PatchUserPaceRegistApi
) : UserRepository {
    override suspend fun joinUser(request: JoinUserRequest): CommonResponse {
        return try {
            val response = registerUserApi.register(request)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }

    }

    override suspend fun getUserData(userId: Int): Response<UserDataResponse> =
        getUserDataApi.getUserData(userId)

    override suspend fun withdrawalUser(userId: Int, secretKey: String): CommonResponse {
        return try {
            val response =
                withdrawalApi.withdrawalUser(userId, WithdrawalUserRequest(secretKey = secretKey))
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }

    override suspend fun patchAlarm(userId: Int, pushOn: Boolean): CommonResponse {
        return try {
            val response = patchAlarmApi.patchAlarm(userId, if (pushOn) "Y" else "N")
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }

    }

    override suspend fun nicknameChange(userId: Int, nickname: String): CommonResponse {
        return try {
            val response = nicknameChangeApi.editNickname(userId, EditNicknameRequest(nickname))
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }

    override suspend fun jobChange(userId: Int, job: String): CommonResponse {
        return try {
            val response = jobChangeApi.editJob(userId, EditJobRequest(job))
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }

    override suspend fun patchUserImage(imageUrl: String?): CommonResponse {
        return try {
            val response = patchUserImageApi.patchUserImg(
                RunnerBeApplication.mTokenPreference.getUserId(),
                PatchUserImgRequest(imageUrl)
            )
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }

    override suspend fun bookMarkStatusChange(
        userId: Int,
        postId: Int,
        whetherAdd: String
    ): CommonResponse {
        return try {
            val response = bookMarkStatusChangeApi.bookMarkStatusChange(
                userId = userId,
                postId = postId,
                whetherAdd = whetherAdd
            )
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }

    override suspend fun patchUserPaceRegist(userId: Int, pace: String): CommonResponse {
        return try {
            val response = patchUserPaceApi.patchUserPaceRegist(userId, PatchUserPaceRegisterRequest(pace))
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999, e.message ?: "error")
        }
    }
}