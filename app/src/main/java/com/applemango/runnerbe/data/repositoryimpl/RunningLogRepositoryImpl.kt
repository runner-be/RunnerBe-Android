package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.network.api.GetOtherUserProfileApi
import com.applemango.runnerbe.data.network.api.runningLog.DeleteRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.GetJoinedRunnerListApi
import com.applemango.runnerbe.data.network.api.runningLog.GetMonthlyRunningLogListApi
import com.applemango.runnerbe.data.network.api.runningLog.GetRunningLogDetailApi
import com.applemango.runnerbe.data.network.api.runningLog.PatchRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.PostRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.PostStampToJoinedRunnerApi
import com.applemango.runnerbe.data.network.request.PostStampRequest
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import javax.inject.Inject

class RunningLogRepositoryImpl @Inject constructor(
    private val deleteRunningLogApi: DeleteRunningLogApi,
    private val getJoinedRunnerListApi: GetJoinedRunnerListApi,
    private val getMonthlyRunningLogListApi: GetMonthlyRunningLogListApi,
    private val getRunningLogDetailApi: GetRunningLogDetailApi,
    private val patchRunningLogApi: PatchRunningLogApi,
    private val postRunningLogApi: PostRunningLogApi,
    private val postStampToJoinedRunnerApi: PostStampToJoinedRunnerApi,
    private val getOtherUserProfileApi: GetOtherUserProfileApi
) : RunningLogRepository {
    override suspend fun getMonthlyRunningLogList(
        userId: Int,
        year: Int,
        month: Int
    ): CommonResponse {
        return try {
            val response = getMonthlyRunningLogListApi.getMonthlyRunningLog(userId, year, month)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!.result)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun postRunningLog(
        userId: Int,
        year: Int,
        month: Int,
        runningLog: RunningLogRequest
    ): CommonResponse {
        return try {
            val response = postRunningLogApi.postRunningLog(userId, year, month, runningLog)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun getRunningLogDetail(userId: Int, logId: Int): CommonResponse {
        return try {
            val response = getRunningLogDetailApi.getRunningLogDetail(userId, logId)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun patchRunningLog(
        userId: Int,
        logId: Int,
        runningLog: RunningLogRequest
    ): CommonResponse {
        return try {
            val response = patchRunningLogApi.patchRunningLog(userId, logId, runningLog)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun deleteRunningLog(userId: Int, logId: Int): CommonResponse {
        return try {
            val response = deleteRunningLogApi.deleteRunningLog(userId, logId)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun getJoinedRunnerList(userId: Int, logId: Int): CommonResponse {
        return try {
            val response = getJoinedRunnerListApi.getJoinedRunnerList(userId, logId)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.result.isNotEmpty()
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun postStampToJoinedRunner(
        userId: Int,
        logId: Int,
        stamp: PostStampRequest
    ): CommonResponse {
        return try {
            val response = postStampToJoinedRunnerApi.postStampToJoinedRunner(userId, logId, stamp)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }

    override suspend fun getOtherUserProfile(targetUserId: Int): CommonResponse {
        return try {
            val response = getOtherUserProfileApi.getOtherUserProfile(targetUserId)
            if (response.isSuccessful
                && response.body() != null
                && response.body()!!.isSuccess
            ) {
                CommonResponse.Success(response.body()!!.code, response.body()!!.result)
            } else {
                CommonResponse.Failed(
                    response.body()?.code ?: response.code(),
                    response.body()?.message ?: response.message()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }
}