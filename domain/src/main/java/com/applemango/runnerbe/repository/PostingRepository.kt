package com.applemango.runnerbe.repository

import androidx.paging.PagingData
import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.entity.BookmarksEntity
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingsEntity
import com.applemango.runnerbe.usecaseImpl.post.GetPostsUseCase.GetRunningListParam
import com.applemango.runnerbe.usecaseImpl.post.WritePostUseCase.WriteRunningParam
import kotlinx.coroutines.flow.Flow

interface PostingRepository {

    suspend fun writeRunning(userId: Int, request : WriteRunningParam) : CommonEntity
    suspend fun postClosing(postId: Int) : CommonEntity
    suspend fun postApply(postId: Int, userId: Int) : CommonEntity
    suspend fun dropPost(postId: Int, userId: Int): CommonEntity
    suspend fun reportPost(postId: Int, userId: Int): CommonEntity

    suspend fun getRunningList(runningTag: String, request: GetRunningListParam) : PostingsEntity
    fun getAddressList(keyword: String): Flow<PagingData<AddressEntity>>
    suspend fun getBookmarkList(userId: Int) : BookmarksEntity
    suspend fun getPostDetail(postId : Int, userId: Int) : PostingDetailEntity
}