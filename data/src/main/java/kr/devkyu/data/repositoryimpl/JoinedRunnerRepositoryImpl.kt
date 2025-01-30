package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.JoinedRunnerMapper
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnerEntity
import com.applemango.runnerbe.repository.JoinedRunnerRepository
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase.PostStampParam
import kotlinx.coroutines.flow.first
import kr.devkyu.data.network.TokenSPreference
import kr.devkyu.data.network.UserDataStore
import kr.devkyu.data.network.api.GetJoinedRunnersApi
import kr.devkyu.data.network.api.PatchAppliedRunnerApi
import kr.devkyu.data.network.api.PatchJoinedRunnerAttendanceApi
import kr.devkyu.data.network.api.PostStampToJoinedRunnerApi
import kr.devkyu.data.network.request.AttendanceAccessionRequest
import kr.devkyu.data.network.request.PostStampRequest
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
        stamp: PostStampParam
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