package com.applemango.runnerbe.domain.usecase.post

import com.applemango.runnerbe.data.network.request.AttendanceAccessionRequest
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AttendanceAccessionUseCase @Inject constructor(private val repo: PostRepository) {

    operator fun invoke(
        postId: Int,
        userIds: List<String>,
        whetherAttendList: List<String>
    ): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            repo.attendanceAccession(
                postId,
                request = AttendanceAccessionRequest(
                    getUserIdsToString(userIds),
                    getWhetherAttendListString(whetherAttendList)
                )
            )
        }.onSuccess {
            emit(it)
        }.onFailure {
            it.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(it.message))
        }
    }

    private fun getUserIdsToString(userIds: List<String>) : String{
        return StringBuilder().apply {
            userIds.forEachIndexed { index, s ->
                this.append(s)
                if (index < userIds.size - 1) this.append(",")
            }
        }.toString()
    }

    private fun getWhetherAttendListString(whetherAttendList: List<String>) : String {
        return StringBuilder().apply {
            whetherAttendList.forEachIndexed { index, s ->
                this.append(s)
                if (index < whetherAttendList.size - 1) this.append(",")
            }
        }.toString()
    }
}