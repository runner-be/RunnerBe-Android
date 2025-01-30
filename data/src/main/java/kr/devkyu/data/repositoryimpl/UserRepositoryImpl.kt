package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.MyPageMapper
import kr.devkyu.data.mapper.NewUserMapper
import kr.devkyu.data.mapper.OtherUserMapper
import kr.devkyu.data.mapper.SocialLoginMapper
import kr.devkyu.data.mapper.UserMapper
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.MyPageEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.repository.UserRepository
import kotlinx.coroutines.flow.first
import kr.devkyu.data.network.UserDataStore
import kr.devkyu.data.network.api.DeleteUserApi
import kr.devkyu.data.network.api.GetOtherUserProfileApi
import kr.devkyu.data.network.api.GetUserDataApi
import kr.devkyu.data.network.api.PatchUserImageApi
import kr.devkyu.data.network.api.PatchUserPaceRegistApi
import kr.devkyu.data.network.api.PostBookmarkedPostApi
import kr.devkyu.data.network.api.PostKakaoLoginAPI
import kr.devkyu.data.network.api.PostNaverLoginAPI
import kr.devkyu.data.network.api.PostNewUserApi
import kr.devkyu.data.network.api.UpdateJobApi
import kr.devkyu.data.network.api.UpdateNicknameApi
import kr.devkyu.data.network.request.EditJobRequest
import kr.devkyu.data.network.request.EditNicknameRequest
import kr.devkyu.data.network.request.JoinUserRequest
import kr.devkyu.data.network.request.PatchUserImgRequest
import kr.devkyu.data.network.request.PatchUserPaceRegisterRequest
import kr.devkyu.data.network.request.SocialLoginRequest
import kr.devkyu.data.network.request.WithdrawalUserRequest
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
    private val userMapper: UserMapper,
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
        userDataStore.logoutSet()
    }

    override suspend fun withdrawalUser(secretKey: String): CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                deleteUserApi.withdrawalUser(userId, WithdrawalUserRequest(secretKey))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun nicknameChange(nickname: String): CommonEntity {
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

    override suspend fun jobChange(job: String): CommonEntity {
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

    override suspend fun patchUserImage(imageUrl: String?): CommonEntity {
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
    ): CommonEntity {
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

    override suspend fun patchUserPaceRegist(pace: String): CommonEntity {
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