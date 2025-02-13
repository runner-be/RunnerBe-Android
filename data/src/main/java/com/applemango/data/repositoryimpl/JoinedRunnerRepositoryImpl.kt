package com.applemango.data.repositoryimpl

import com.applemango.data.mapper.CommonMapper
import com.applemango.data.mapper.JoinedRunnerMapper
import com.applemango.data.network.UserDataStore
import com.applemango.data.network.api.GetJoinedRunnersApi
import com.applemango.data.network.api.PatchAppliedRunnerApi
import com.applemango.data.network.api.PatchJoinedRunnerAttendanceApi
import com.applemango.data.network.api.PostStampToJoinedRunnerApi
import com.applemango.data.network.request.AttendanceAccessionRequest
import com.applemango.data.network.request.PostStampRequest
import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.JoinedRunnerEntity
import com.applemango.domain.repository.JoinedRunnerRepository
import com.applemango.domain.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.domain.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class JoinedRunnerRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
    private val patchJoinedRunnerAttendanceApi: PatchJoinedRunnerAttendanceApi,
    private val patchAppliedRunnerApi: PatchAppliedRunnerApi,
    private val getJoinedRunnersApi: GetJoinedRunnersApi,
    private val postStampToJoinedRunnerApi: PostStampToJoinedRunnerApi,
    private val commonMapper: CommonMapper,
    private val joinedRunnerMapper: JoinedRunnerMapper,
) : BaseRepository(), JoinedRunnerRepository {

    override suspend fun attendanceAccession(
        postId: Int,
        request: AttendanceAccessionUseCase.AttendanceAccessionParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                patchJoinedRunnerAttendanceApi.attendanceAccession(
                    postId,
                    AttendanceAccessionRequest(
                        request.userIds,
                        request.attendList
                    )
                )
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun postWhetherAccept(
        postId: Int,
        applicantId: Int,
        whetherAccept: String
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                patchAppliedRunnerApi.whetherAccept(postId, applicantId, whetherAccept)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun postStampToJoinedRunner(
        logId: Int,
        stamp: WriteStampToJoinedRunnerUseCase.PostStampParam
    ): CommonEntity {
        val userId = userDataStore.getUserId().first()
        return handleApiCall(
            apiCall = {
                postStampToJoinedRunnerApi.postStampToJoinedRunner(
                    userId, logId, PostStampRequest(
                        stamp.targetUserId,
                        stamp.stampCode
                    )
                )
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getJoinedRunnerList(logId: Int): List<JoinedRunnerEntity> {
        val userId = userDataStore.getUserId().first()
        val response = getJoinedRunnersApi.getJoinedRunnerList(userId, logId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return body.result.map {
                    joinedRunnerMapper.mapToDomain(it)
                }
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}