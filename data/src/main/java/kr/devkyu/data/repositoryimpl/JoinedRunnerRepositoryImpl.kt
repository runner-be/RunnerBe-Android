package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.data.network.api.GetJoinedRunnersApi
import com.applemango.runnerbe.data.network.api.PatchAppliedRunnerApi
import com.applemango.runnerbe.data.network.api.PatchJoinedRunnerAttendanceApi
import com.applemango.runnerbe.data.network.api.PostStampToJoinedRunnerApi
import com.applemango.runnerbe.data.network.request.AttendanceAccessionRequest
import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnerEntity
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
        userId: Int,
        logId: Int,
        stamp: PostStampParam
    ): CommonEntity {
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

    override suspend fun getJoinedRunnerList(userId: Int, logId: Int): List<JoinedRunnerEntity> {
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