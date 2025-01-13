package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.PostingRepository
import javax.inject.Inject

class WritePostUseCase @Inject constructor(
    private val repo: PostingRepository
) {

    suspend operator fun invoke(userId : Int, body : WriteRunningParam) : CommonEntity {
        return repo.writeRunning(userId, body)
    }

    data class WriteRunningParam(
        val runningTitle: String,
        val gatheringTime : String,
        val runningTime: String,
        val latitude: Double,
        val longitude : Double,
        val placeName : String,
        val placeAddress : String,
        val placeExplain : String,
        val runningTag : String,
        val minAge : Int,
        val maxAge : Int,
        val numberOfRunner : Int,
        val contents : String?,
        val gender : String,
        val paceGrade: String,
        val isAfterParty: Int
    )
}