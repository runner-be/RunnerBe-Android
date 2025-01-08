package kr.devkyu.domain.usecaseImpl.post

import kr.devkyu.domain.entity.BaseEntity
import kr.devkyu.domain.repository.PostRepository
import javax.inject.Inject

class WritePostUseCase @Inject constructor(
    private val repo: PostRepository
) {

    suspend operator fun invoke(userId : Int, body : WriteRunningParam) : BaseEntity {
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