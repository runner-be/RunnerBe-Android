package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.data.mapper.NewUserMapper
import com.applemango.runnerbe.data.mapper.OtherUserMapper
import com.applemango.runnerbe.data.mapper.SocialLoginMapper
import com.applemango.runnerbe.data.mapper.UserMapper
import com.applemango.runnerbe.data.network.api.*
import com.applemango.runnerbe.data.network.request.EditJobRequest
import com.applemango.runnerbe.data.network.request.EditNicknameRequest
import com.applemango.runnerbe.data.network.request.JoinUserRequest
import com.applemango.runnerbe.data.network.request.PatchUserImgRequest
import com.applemango.runnerbe.data.network.request.PatchUserPaceRegisterRequest
import com.applemango.runnerbe.data.network.request.SocialLoginRequest
import com.applemango.runnerbe.data.network.request.WithdrawalUserRequest
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val kakaoLoginAPI: PostKakaoLoginAPI,
    private val naverLoginAPI: PostNaverLoginAPI,
    private val postNewUserApi: PostNewUserApi,
    private val getUserDataApi: GetUserDataApi,
    private val deleteUserApi: DeleteUserApi,
    private val updateNicknameApi: UpdateNicknameApi,
    private val jobChangeApi: UpdateJobApi,
    private val patchUserImageApi: PatchUserImageApi,
    private val postBookmarkedPostApi: PostBookmarkedPostApi,
    private val patchUserPaceApi: PatchUserPaceRegistApi,
    private val getOtherUserProfileApi: GetOtherUserProfileApi,
    private val commonMapper: CommonMapper,
    private val socialLoginMapper: SocialLoginMapper,
    private val newUserMapper: NewUserMapper,
    private val userMapper: UserMapper,
    private val otherUserMapper: OtherUserMapper,
) : BaseRepository(), UserRepository {
    override suspend fun withdrawalUser(userId: Int, secretKey: String): CommonEntity {
        return handleApiCall(
            apiCall = {
                deleteUserApi.withdrawalUser(userId, WithdrawalUserRequest(secretKey))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun nicknameChange(userId: Int, nickname: String): CommonEntity {
        return handleApiCall(
            apiCall = {
                updateNicknameApi.editNickname(userId, EditNicknameRequest(nickname))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun jobChange(userId: Int, job: String): CommonEntity {
        return handleApiCall(
            apiCall = {
                jobChangeApi.editJob(userId, EditJobRequest(job))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun patchUserImage(userId: Int, imageUrl: String?): CommonEntity {
        return handleApiCall(
            apiCall = {
                patchUserImageApi.patchUserImg(userId, PatchUserImgRequest(imageUrl))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun bookMarkStatusChange(
        userId: Int,
        postId: Int,
        whetherAdd: String
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                postBookmarkedPostApi.bookMarkStatusChange(userId, whetherAdd, postId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun patchUserPaceRegist(userId: Int, pace: String): CommonEntity {
        return handleApiCall(
            apiCall = {
                patchUserPaceApi.patchUserPaceRegist(userId, PatchUserPaceRegisterRequest(pace))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun kakaoLogin(accessToken: String): SocialLoginEntity {
        val response = kakaoLoginAPI.kakaoLogin(SocialLoginRequest(accessToken))
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return socialLoginMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun naverLogin(accessToken: String): SocialLoginEntity {
        val response = naverLoginAPI.naverLogin(SocialLoginRequest(accessToken))
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return socialLoginMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun joinUser(
        uuid: String,
        nickName: String?,
        birthday: Int,
        genderTag: String,
        jobTag: String,
        deviceToken: String
    ): NewUserEntity {
        val response = postNewUserApi.register(
            JoinUserRequest(uuid, nickName, birthday, genderTag, jobTag, deviceToken)
        )
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return newUserMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getUserData(userId: Int): UserEntity {
        val response = getUserDataApi.getUserData(userId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return userMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getOtherUserProfile(targetUserId: Int): OtherUserEntity {
        val response = getOtherUserProfileApi.getOtherUserProfile(targetUserId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return otherUserMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}