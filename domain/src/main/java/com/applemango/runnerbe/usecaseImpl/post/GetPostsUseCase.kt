package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.PostingsEntity
import com.applemango.runnerbe.repository.PostRepository
import javax.inject.Inject

/**
 * 게시글 목록 조회
 */
class GetPostsUseCase @Inject constructor(
    private val repo: PostRepository
) {
    suspend operator fun invoke(runningTag: String, request : GetRunningListParam) : PostingsEntity {
        return repo.getRunningList(runningTag = runningTag, request = request)
    }

    data class GetRunningListParam(
        val whetherEnd: String,
        val priorityFilter : String,
        val paceFilter : String,
        val distanceFilter : String,
        val gender : String,
        val maxAge : String,
        val minAge : String,
        val jobFilter : String,
        val userLng : Double,
        val userLat : Double,
        val afterPartyFilter: String,
        val keyword : String = "N",
        val userId : Int?,
        val pageSize : Int,
        val page : Int
    )
}