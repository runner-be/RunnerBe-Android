package com.applemango.data.repositoryimpl

import com.applemango.data.mapper.CommonMapper
import com.applemango.data.mapper.MyPageMapper
import com.applemango.data.mapper.NewUserMapper
import com.applemango.data.mapper.OtherUserMapper
import com.applemango.data.mapper.SocialLoginMapper
import com.applemango.data.network.UserDataStore
import com.applemango.data.network.api.DeleteUserApi
import com.applemango.data.network.api.GetOtherUserProfileApi
import com.applemango.data.network.api.GetUserDataApi
import com.applemango.data.network.api.PatchUserImageApi
import com.applemango.data.network.api.PatchUserPaceRegistApi
import com.applemango.data.network.api.PostBookmarkedPostApi
import com.applemango.data.network.api.PostKakaoLoginAPI
import com.applemango.data.network.api.PostNaverLoginAPI
import com.applemango.data.network.api.PostNewUserApi
import com.applemango.data.network.api.UpdateJobApi
import com.applemango.data.network.api.UpdateNicknameApi
import com.applemango.data.network.request.EditJobRequest
import com.applemango.data.network.request.EditNicknameRequest
import com.applemango.data.network.request.JoinUserRequest
import com.applemango.data.network.request.PatchUserImgRequest
import com.applemango.data.network.request.PatchUserPaceRegisterRequest
import com.applemango.data.network.request.SocialLoginRequest
import com.applemango.data.network.request.WithdrawalUserRequest
import com.applemango.domain.entity.MyPageEntity
import com.applemango.domain.entity.OtherUserEntity
import com.applemango.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
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
    private val myPageMapper: MyPageMapper,
    private val otherUserMapper: OtherUserMapper,
) : BaseRepository(), UserRepository {
    private var cachedUserId: Int? = null

    override suspend fun getUserId(): Int {
        if (cachedUserId == -1 || cachedUserId == null) {
            cachedUserId = userDataStore.getUserId().first()
        }
        return cachedUserId!!
    }

    override suspend fun getJwtToken(): String? {
        return userDataStore.getJwtToken()
    }

    override suspend fun getUserPace(): String {
        return userDataStore.getPace().first()
    }

    override suspend fun getDeviceToken(): String? {
        return userDataStore.getDeviceToken().first()
    }

    override suspend fun getUuid(): String? {
        return userDataStore.getUuid().first()
    }

    override suspend fun updateUserPace(pace: String) {
        userDataStore.setRunningPace(pace)
    }

    override suspend fun updateJwtToken(jwtToken: String) {
        userDataStore.setJwtToken(jwtToken)
    }

    override suspend fun updateUserId(userId: Int) {
        userDataStore.setUserId(userId)
    }

    override suspend fun updateUuid(uuid: String) {
        userDataStore.setUuid(uuid)
    }

    override suspend fun updateLoginType(loginType: Int) {
        userDataStore.setLoginType(loginType)
    }

    override suspend fun logout() {
        cachedUserId = null
        userDataStore.logoutSet()
    }

    override suspend fun withdrawalUser(secretKey: String): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                val response = deleteUserApi.withdrawalUser(userId, WithdrawalUserRequest(secretKey))
                if (response.isSuccessful) {
                    logout()
                }
                response
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun nicknameChange(nickname: String): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                updateNicknameApi.editNickname(userId, EditNicknameRequest(nickname))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun jobChange(job: String): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                jobChangeApi.editJob(userId, EditJobRequest(job))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun patchUserImage(imageUrl: String?): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
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
        postId: Int,
        whetherAdd: String
    ): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                postBookmarkedPostApi.bookMarkStatusChange(userId, whetherAdd, postId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun patchUserPaceRegist(pace: String): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                patchUserPaceApi.patchUserPaceRegist(userId, PatchUserPaceRegisterRequest(pace))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun kakaoLogin(accessToken: String): com.applemango.domain.entity.SocialLoginEntity {
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

    override suspend fun naverLogin(accessToken: String): com.applemango.domain.entity.SocialLoginEntity {
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
    ): com.applemango.domain.entity.NewUserEntity {
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

    override suspend fun getUserData(): MyPageEntity {
        val userId = getUserId()
        val response = getUserDataApi.getUserData(userId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return myPageMapper.mapToDomain(body)
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