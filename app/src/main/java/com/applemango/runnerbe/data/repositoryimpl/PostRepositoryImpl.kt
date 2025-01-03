package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.network.api.*
import com.applemango.runnerbe.data.network.request.AttendanceAccessionRequest
import com.applemango.runnerbe.data.network.request.GetRunningListRequest
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import retrofit2.HttpException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val getBookmarkApi: GetBookmarkApi,
    private val postRunningApi: PostRunningApi,
    private val getRunningListApi: GetRunningListApi,
    private val patchJoinedRunnerAttendanceApi: PatchJoinedRunnerAttendanceApi,
    private val getPostDetailApi: GetPostDetailApi,
    private val postClosingApi: PostClosingApi,
    private val postApplyToPostApi: PostApplyToPostApi,
    private val postPatchAppliedRunnerApi: PatchAppliedRunnerApi,
    private val deletePostApi: DeletePostApi,
    private val reportPostApi: PostReportPostingApi,
    private val getAddressResultListApi: GetAddressResultListApi
) : PostRepository {
    override suspend fun getBookmarkList(userId: Int): CommonResponse {
        return try {
            val response = getBookmarkApi.postBookmark(userId)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun writeRunning(userId: Int, request: WriteRunningRequest): CommonResponse {
        return try {
            val response = postRunningApi.writingRunning(userId, request)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun getRunningList(
        runningTag: String,
        request: GetRunningListRequest
    ): CommonResponse {
        return try {
            val response = getRunningListApi.getRunningList(
                userId = request.userId,
                runningTag = runningTag,
                distanceFilter = request.distanceFilter,
                gender = request.gender,
                paceFilter = request.paceFilter,
                jobFilter = request.jobFilter,
                minAge = request.minAge,
                maxAge = request.maxAge,
                afterPartyFilter = request.afterPartyFilter,
                priorityFilter = request.priorityFilter,
                userLat = request.userLat,
                userLng = request.userLng,
                whetherEnd = request.whetherEnd,
                pageSize = request.pageSize,
                page = request.page
            )
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun attendanceAccession(
        postId: Int,
        request: AttendanceAccessionRequest
    ): CommonResponse {
        return try {
            val response = patchJoinedRunnerAttendanceApi.attendanceAccession(postId, request)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun getPostDetail(postId: Int, userId: Int): CommonResponse {
        return try {
            val response = getPostDetailApi.getPostDetail(postId, userId)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun postClosing(postId: Int): CommonResponse {
        return try {
            val response = postClosingApi.postClose(postId)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun postApply(postId: Int, userId: Int): CommonResponse {
        return try {
            val response = postApplyToPostApi.postApply(postId, userId)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun postWhetherAccept(
        postId: Int,
        applicantId: Int,
        whetherAccept: String
    ): CommonResponse {
        return try {
            val response = postPatchAppliedRunnerApi.whetherAccept(postId, applicantId, whetherAccept)
            if (response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun getAddressList(keyword: String): CommonResponse {
        return try {
            val response = getAddressResultListApi.getAddressList(query = keyword)

            if (response.isSuccessful) {
                CommonResponse.Success(response.code(), response)
            } else {
                CommonResponse.Failed(
                    response.code(),
                    response.message() ?: "failed"
                )
            }
        } catch (e: HttpException) {
            CommonResponse.Failed(e.code(), e.message ?: "HTTP error")
        } catch (e: Exception) {
            CommonResponse.Failed.getDefaultFailed(e.message)
        }
    }
    override suspend fun dropPost(postId: Int, userId: Int): CommonResponse {
        return try {
            val response = deletePostApi.dropPost(postId, userId)
            if(response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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

    override suspend fun reportPost(postId: Int, userId: Int): CommonResponse {
        return try {
            val response = reportPostApi.reportPosting(postId, userId)
            if(response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
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
}