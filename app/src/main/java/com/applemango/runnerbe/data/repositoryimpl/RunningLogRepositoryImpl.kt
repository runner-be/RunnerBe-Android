package com.applemango.runnerbe.data.repositoryimpl

import android.util.Log
import com.applemango.runnerbe.data.network.api.runningLog.DeleteRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.GetJoinedRunnerListApi
import com.applemango.runnerbe.data.network.api.runningLog.GetMonthlyRunningLogListApi
import com.applemango.runnerbe.data.network.api.runningLog.GetRunningLogDetailApi
import com.applemango.runnerbe.data.network.api.runningLog.GetStampListApi
import com.applemango.runnerbe.data.network.api.runningLog.PatchRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.PatchStampToJoinedRunnerApi
import com.applemango.runnerbe.data.network.api.runningLog.PostRunningLogApi
import com.applemango.runnerbe.data.network.api.runningLog.PostStampToJoinedRunnerApi
import com.applemango.runnerbe.data.network.request.PatchStampRequest
import com.applemango.runnerbe.data.network.request.RunningLogRequest
import com.applemango.runnerbe.domain.repository.RunningLogRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import javax.inject.Inject

class RunningLogRepositoryImpl @Inject constructor(
    private val deleteRunningLogApi: DeleteRunningLogApi,
    private val getJoinedRunnerListApi: GetJoinedRunnerListApi,
    private val getMonthlyRunningLogListApi: GetMonthlyRunningLogListApi,
    private val getRunningLogDetailApi: GetRunningLogDetailApi,
    private val getStampListApi: GetStampListApi,
    private val patchRunningLogApi: PatchRunningLogApi,
    private val patchStampToJoinedRunnerApi: PatchStampToJoinedRunnerApi,
    private val postRunningLogApi: PostRunningLogApi,
    private val postStampToJoinedRunnerApi: PostStampToJoinedRunnerApi
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
            CommonResponse.Failed(999, e.message ?: "getMonthlyRunningLogList failed")
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
            CommonResponse.Failed(999, e.message ?: "postRunningLog failed")
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
            CommonResponse.Failed(999, e.message ?: "getRunningLogDetail failed")
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
            CommonResponse.Failed(999, e.message ?: "patchRunningLog failed")
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
            CommonResponse.Failed(999, e.message ?: "deleteRunningLog failed")
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
            CommonResponse.Failed(999, e.message ?: "getJoinedRunnerList failed")
        }
    }

    override suspend fun getStampList(userId: Int, year: Int, month: Int): CommonResponse {
        return try {
            val response = getStampListApi.getStampList(userId, year, month)
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
            CommonResponse.Failed(999, e.message ?: "getStampList failed")
        }
    }

    override suspend fun patchStampToJoinedRunner(
        userId: Int,
        logId: Int,
        stamp: PatchStampRequest
    ): CommonResponse {
        return try {
            val response =
                patchStampToJoinedRunnerApi.patchStampToJoinedRunner(userId, logId, stamp)
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
            CommonResponse.Failed(999, e.message ?: "patchStampToJoinedRunner failed")
        }
    }

    override suspend fun postStampToJoinedRunner(
        userId: Int,
        logId: Int,
        stamp: PatchStampRequest
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
            CommonResponse.Failed(999, e.message ?: "postStampToJoinedRunner failed")
        }
    }
}