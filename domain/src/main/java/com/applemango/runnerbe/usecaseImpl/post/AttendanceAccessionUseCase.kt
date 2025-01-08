package com.applemango.runnerbe.usecaseImpl.post

import com.applemango.runnerbe.entity.BaseEntity
import com.applemango.runnerbe.repository.PostRepository
import javax.inject.Inject

class AttendanceAccessionUseCase @Inject constructor(
    private val repo: PostRepository
) {

    data class AttendanceAccessionParam(
        val userIds: String, //"1,2,3" 형태의 String으로 서버에서 받음
        val attendList : String // "Y,N" 형태의 String으로 서버에서 받음
    )

    suspend operator fun invoke(
        postId: Int,
        userIds: List<String>,
        whetherAttendList: List<String>
    ): BaseEntity {
        return repo.attendanceAccession(
            postId,
            request = AttendanceAccessionParam(
                getUserIdsToString(userIds),
                getWhetherAttendListString(whetherAttendList)
            )
        )
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