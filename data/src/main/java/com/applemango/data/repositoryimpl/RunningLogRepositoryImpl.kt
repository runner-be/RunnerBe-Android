package com.applemango.data.repositoryimpl

import com.applemango.data.mapper.CommonMapper
import com.applemango.data.mapper.MonthlyRunningLogMapper
import com.applemango.data.mapper.RunningLogDetailMapper
import com.applemango.data.network.UserDataStore
import com.applemango.data.network.api.DeleteRunningLogApi
import com.applemango.data.network.api.GetMonthlyRunningLogsApi
import com.applemango.data.network.api.GetRunningLogDetailApi
import com.applemango.data.network.api.PatchRunningLogApi
import com.applemango.data.network.api.PostRunningLogApi
import com.applemango.data.network.request.RunningLogRequest
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.entity.RunningLogDetailEntity
import com.applemango.domain.repository.RunningLogRepository
import com.applemango.domain.usecaseImpl.runninglog.UpdateRunningLogUseCase
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import javax.inject.Inject

class RunningLogRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
    private val postRunningLogApi: PostRunningLogApi,
    private val deleteRunningLogApi: DeleteRunningLogApi,
    private val getRunningLogDetailApi: GetRunningLogDetailApi,
    private val patchRunningLogApi: PatchRunningLogApi,
    private val getMonthlyRunningLogsApi: GetMonthlyRunningLogsApi,
    private val commonMapper: CommonMapper,
    private val monthlyRunningLogMapper: MonthlyRunningLogMapper,
    private val runningLogDetailMapper: RunningLogDetailMapper,
) : BaseRepository(), RunningLogRepository {
    private var cachedUserId: Int? = null

    private suspend fun getUserId(): Int {
        if (cachedUserId == -1 || cachedUserId == null) {
            cachedUserId = userDataStore.getUserId().first()
        }
        return cachedUserId!!
    }

    override suspend fun postRunningLog(
        year: Int,
        month: Int,
        runningLog: UpdateRunningLogUseCase.RunningLogParam
    ): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
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
        logId: Int,
        runningLog: UpdateRunningLogUseCase.RunningLogParam
    ): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
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
        logId: Int
    ): com.applemango.domain.entity.CommonEntity {
        val userId = getUserId()
        return handleApiCall(
            apiCall = {
                deleteRunningLogApi.deleteRunningLog(userId, logId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getRunningLogDetail(
        targetUserId: Int,
        logId: Int
    ): RunningLogDetailEntity {
        val userId = getUserId()
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

    override suspend fun getMonthlyRunningLogs(
        targetUserId: Int,
        year: Int,
        month: Int
    ): MonthlyRunningLogEntity {
        val response = getMonthlyRunningLogsApi.getMonthlyRunningLog(targetUserId, year, month)
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
}