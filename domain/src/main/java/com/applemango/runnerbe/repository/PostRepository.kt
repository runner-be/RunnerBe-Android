package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.entity.BookmarksEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.PostingsEntity

interface PostRepository {

    suspend fun getBookmarkList(userId: Int) : BookmarksEntity
    suspend fun writeRunning(userId: Int, request : WriteRunningRequest) : BaseEntity
    suspend fun getRunningList(runningTag: String, request: GetRunningListRequest) : PostingsEntity
    suspend fun attendanceAccession(postId: Int, request: AttendanceAccessionRequest) : BaseEntity
    suspend fun getPostDetail(postId : Int, userId: Int) : PostingEntity
    suspend fun postClosing(postId: Int) : BaseEntity
    suspend fun postApply(postId: Int, userId: Int) : BaseEntity
    suspend fun postWhetherAccept(postId: Int, applicantId: Int, whetherAccept : String) : BaseEntity
    suspend fun getAddressList(keyword: String): AddressEntity

    suspend fun dropPost(postId: Int, userId: Int): BaseEntity
    suspend fun reportPost(postId: Int, userId: Int): BaseEntity
}