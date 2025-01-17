package com.applemango.runnerbe.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.data.mapper.PostingDetailMapper
import com.applemango.runnerbe.data.mapper.PostingMapper
import com.applemango.runnerbe.data.network.api.DeletePostApi
import com.applemango.runnerbe.data.network.api.GetBookmarksApi
import com.applemango.runnerbe.data.network.api.GetPostDetailApi
import com.applemango.runnerbe.data.network.api.GetRunningListApi
import com.applemango.runnerbe.data.network.api.PostApplyToPostApi
import com.applemango.runnerbe.data.network.api.PostClosingApi
import com.applemango.runnerbe.data.network.api.PostReportPostingApi
import com.applemango.runnerbe.data.network.api.PostRunningApi
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.data.paging.AddressSearchPagingSource
import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.repository.PostingRepository
import com.applemango.runnerbe.usecaseImpl.post.GetPostsUseCase.GetRunningListParam
import com.applemango.runnerbe.usecaseImpl.post.WritePostUseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class PostingRepositoryImpl @Inject constructor(
    private val getBookmarksApi: GetBookmarksApi,
    private val postRunningApi: PostRunningApi,
    private val getRunningListApi: GetRunningListApi,
    private val getPostDetailApi: GetPostDetailApi,
    private val postClosingApi: PostClosingApi,
    private val postApplyToPostApi: PostApplyToPostApi,
    private val deletePostApi: DeletePostApi,
    private val reportPostApi: PostReportPostingApi,
    private val addressSearchPagingSource: AddressSearchPagingSource,
    private val commonMapper: CommonMapper,
    private val postingMapper: PostingMapper,
    private val postingDetailMapper: PostingDetailMapper,
) : BaseRepository(), PostingRepository {

    override suspend fun writeRunning(
        userId: Int,
        request: WritePostUseCase.WriteRunningParam
    ): CommonEntity {
        return handleApiCall(
            apiCall = {
                postRunningApi.writingRunning(userId,
                    WriteRunningRequest(
                        runningTitle = request.runningTitle,
                        gatheringTime = request.gatheringTime,
                        runningTime = request.runningTime,
                        latitude = request.latitude,
                        longitude = request.longitude,
                        placeName = request.placeName,
                        placeAddress = request.placeAddress,
                        placeExplain = request.placeExplain,
                        runningTag = request.runningTag,
                        minAge = request.minAge,
                        maxAge = request.maxAge,
                        numberOfRunner = request.numberOfRunner,
                        contents = request.contents,
                        gender = request.gender,
                        paceGrade = request.paceGrade,
                        isAfterParty = request.isAfterParty
                    )
                )
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun postClosing(postId: Int): CommonEntity {
        return handleApiCall(
            apiCall = {
                postClosingApi.postClose(postId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun postApply(postId: Int, userId: Int): CommonEntity {
        return handleApiCall(
            apiCall = {
                postApplyToPostApi.postApply(postId, userId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }
    override suspend fun dropPost(postId: Int, userId: Int): CommonEntity {
        return handleApiCall(
            apiCall = {
                deletePostApi.dropPost(postId, userId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun reportPost(postId: Int, userId: Int): CommonEntity {
        return handleApiCall(
            apiCall = {
                reportPostApi.reportPosting(postId, userId)
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getBookmarkList(userId: Int): List<PostingEntity> {
        val response = getBookmarksApi.getBookmarks(userId)
        if (response.isSuccessful) {
            val body = response.body()
            val postings = body?.result?.bookMarkList?.map {
                postingMapper.mapToDomain(it)
            }
            if (body?.isSuccess == true) {
                return postings ?: emptyList()
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getRunningList(
        runningTag: String,
        request: GetRunningListParam
    ): List<PostingEntity> {
        val response = getRunningListApi.getRunningList(
            runningTag = runningTag,
            whetherEnd = request.whetherEnd,
            priorityFilter = request.priorityFilter,
            paceFilter = request.paceFilter,
            distanceFilter = request.distanceFilter,
            gender = request.gender,
            maxAge = request.maxAge,
            minAge = request.minAge,
            jobFilter = request.jobFilter,
            userLng = request.userLng,
            userLat = request.userLat,
            afterPartyFilter = request.afterPartyFilter,
            keyword = request.keyword,
            userId = request.userId,
            pageSize = request.pageSize,
            page = request.page
        )
        if (response.isSuccessful) {
            val body = response.body()
            val result = body?.runningList?.map { posting ->
                postingMapper.mapToDomain(posting)
            } ?: emptyList()
            if (body?.isSuccess == true) {
                return result
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getPostDetail(postId: Int, userId: Int): PostingDetailEntity {
        val response = getPostDetailApi.getPostDetail(postId, userId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return postingDetailMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override fun getAddressList(keyword: String): Flow<PagingData<AddressEntity>> {
        return Pager(PagingConfig(pageSize = 20)) {
            addressSearchPagingSource.apply {
                this.query = keyword
            }
        }.flow
    }
}