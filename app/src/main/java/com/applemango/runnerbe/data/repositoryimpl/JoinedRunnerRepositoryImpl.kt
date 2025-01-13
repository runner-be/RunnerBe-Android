package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.data.mapper.JoinedRunnerAttendanceMapper
import com.applemango.runnerbe.data.mapper.JoinedRunnersMapper
import com.applemango.runnerbe.data.network.api.GetJoinedRunnersApi
import com.applemango.runnerbe.data.network.api.PatchAppliedRunnerApi
import com.applemango.runnerbe.data.network.api.PatchJoinedRunnerAttendanceApi
import com.applemango.runnerbe.data.network.api.PostStampToJoinedRunnerApi
import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnersEntity
import com.applemango.runnerbe.repository.JoinedRunnerRepository
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase.PostStampParam
import retrofit2.HttpException
import javax.inject.Inject

class JoinedRunnerRepositoryImpl @Inject constructor(
    private val patchJoinedRunnerAttendanceApi: PatchJoinedRunnerAttendanceApi,
    private val patchAppliedRunnerApi: PatchAppliedRunnerApi,
    private val getJoinedRunnersApi: GetJoinedRunnersApi,
    private val postStampToJoinedRunnerApi: PostStampToJoinedRunnerApi,
    private val commonMapper: CommonMapper,
    private val joinedRunnerAttendanceMapper: JoinedRunnerAttendanceMapper,
    private val joinedRunnersMapper: JoinedRunnersMapper,
    ): BaseRepository(), JoinedRunnerRepository {
    override suspend fun attendanceAccession(
        postId: Int,
        request: AttendanceAccessionUseCase.AttendanceAccessionParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                val domainRequest = joinedRunnerAttendanceMapper.mapToData(request)
                patchJoinedRunnerAttendanceApi.attendanceAccession(postId, domainRequest)
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
        userId: Int,
        logId: Int,
        stamp: PostStampParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                postStampToJoinedRunnerApi.postStampToJoinedRunner(userId, logId, PostStampRequest(
                    stamp.targetUserId,
                    stamp.stampCode
                ))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getJoinedRunnerList(userId: Int, logId: Int): List<JoinedRunnersEntity> {
        val response = getJoinedRunnersApi.getJoinedRunnerList(userId, logId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return joinedRunnersMapper.mapToDomain(body.result)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}