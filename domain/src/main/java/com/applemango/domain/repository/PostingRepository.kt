package com.applemango.domain.repository

import androidx.paging.PagingData
import com.applemango.domain.entity.AddressEntity
import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.PostingDetailEntity
import com.applemango.domain.entity.PostingEntity
import com.applemango.domain.usecaseImpl.post.GetPostsUseCase
import com.applemango.domain.usecaseImpl.post.WritePostUseCase
import kotlinx.coroutines.flow.Flow

interface PostingRepository {

    suspend fun writeRunning(request : WritePostUseCase.WriteRunningParam) : CommonEntity
    suspend fun postClosing(postId: Int) : CommonEntity
    suspend fun postApply(postId: Int) : CommonEntity
    suspend fun dropPost(postId: Int): CommonEntity
    suspend fun reportPost(postId: Int): CommonEntity

    suspend fun getRunningList(runningTag: String, request: GetPostsUseCase.GetRunningListParam) : List<PostingEntity>
    fun getAddressList(keyword: String): Flow<PagingData<AddressEntity>>
    suspend fun getBookmarkList() :List<PostingEntity>
    suspend fun getPostDetail(postId : Int) : PostingDetailEntity
}