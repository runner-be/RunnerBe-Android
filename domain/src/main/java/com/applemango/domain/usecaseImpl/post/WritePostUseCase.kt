package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.PostingRepository
import javax.inject.Inject

class WritePostUseCase @Inject constructor(
    private val repo: PostingRepository
) {

    suspend operator fun invoke(body : WriteRunningParam) : CommonEntity {
        return repo.writeRunning(body)
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