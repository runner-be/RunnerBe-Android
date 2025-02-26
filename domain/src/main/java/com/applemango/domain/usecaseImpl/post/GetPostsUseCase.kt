package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.PostingEntity
import com.applemango.domain.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 게시글 목록 조회
 */
class GetPostsUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(runningTag: String, request : GetRunningListParam) : Flow<List<PostingEntity>> = flow {
        emit(repository.getRunningList(runningTag = runningTag, request = request))
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
        val pageSize : Int,
        val page : Int
    )
}