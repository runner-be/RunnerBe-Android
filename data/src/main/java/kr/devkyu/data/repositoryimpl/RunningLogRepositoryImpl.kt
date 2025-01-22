package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.MonthlyRunningLogMapper
import kr.devkyu.data.mapper.RunningLogDetailMapper
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.repository.RunningLogRepository
import com.applemango.runnerbe.usecaseImpl.runninglog.UpdateRunningLogUseCase.RunningLogParam
import kr.devkyu.data.network.api.DeleteRunningLogApi
import kr.devkyu.data.network.api.GetMonthlyRunningLogsApi
import kr.devkyu.data.network.api.GetRunningLogDetailApi
import kr.devkyu.data.network.api.PatchRunningLogApi
import kr.devkyu.data.network.api.PostRunningLogApi
import kr.devkyu.data.network.request.RunningLogRequest
import retrofit2.HttpException
import javax.inject.Inject

class RunningLogRepositoryImpl @Inject constructor(
    private val postRunningLogApi: PostRunningLogApi,
    private val deleteRunningLogApi: DeleteRunningLogApi,
    private val getRunningLogDetailApi: GetRunningLogDetailApi,
    private val patchRunningLogApi: PatchRunningLogApi,
    private val getMonthlyRunningLogsApi: GetMonthlyRunningLogsApi,
    private val commonMapper: CommonMapper,
    private val monthlyRunningLogMapper: MonthlyRunningLogMapper,
    private val runningLogDetailMapper: RunningLogDetailMapper,
    ) : BaseRepository(), RunningLogRepository {

    override suspend fun postRunningLog(
        userId: Int,
        year: Int,
        month: Int,
        runningLog: RunningLogParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                postRunningLogApi.postRunningLog(
                    userId, year, month,
                    RunningLogRequest(
                        runningLog.runningDate,
                        runningLog.stampCode,
                        runningLog.gatheringId,
                        runningLog.contents,
                        runningLog.imageUrl,
                        runningLog.weatherDegree,
                        runningLog.weatherIcon,
                        runningLog.isOpened,
                    )
                )
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun patchRunningLog(
        userId: Int,
        logId: Int,
        runningLog: RunningLogParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                patchRunningLogApi.patchRunningLog(
                    userId, logId,
                    RunningLogRequest(
                        runningLog.runningDate,
                        runningLog.stampCode,
                        runningLog.gatheringId,
                        runningLog.contents,
                        runningLog.imageUrl,
                        runningLog.weatherDegree,
                        runningLog.weatherIcon,
                        runningLog.isOpened,
                    )
                )
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun deleteRunningLog(
        userId: Int,
        logId: Int
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                deleteRunningLogApi.deleteRunningLog(userId, logId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getMonthlyRunningLogs(
        userId: Int,
        year: Int,
        month: Int
    ): MonthlyRunningLogEntity {
        val response = getMonthlyRunningLogsApi.getMonthlyRunningLog(userId, year, month)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return monthlyRunningLogMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getRunningLogDetail(userId: Int, logId: Int): RunningLogDetailEntity {
        val response = getRunningLogDetailApi.getRunningLogDetail(userId, logId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return runningLogDetailMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}