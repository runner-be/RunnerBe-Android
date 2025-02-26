package com.applemango.domain.repository

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.entity.JoinedRunnerEntity
import com.applemango.domain.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.domain.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase

interface JoinedRunnerRepository {
    suspend fun attendanceAccession(
        postId: Int,
        request: AttendanceAccessionUseCase.AttendanceAccessionParam
    ): CommonEntity

    suspend fun postWhetherAccept(
        postId: Int,
        applicantId: Int,
        whetherAccept: String
    ): CommonEntity

    suspend fun getJoinedRunnerList(logId: Int): List<JoinedRunnerEntity>
    suspend fun postStampToJoinedRunner(
        logId: Int,
        stamp: WriteStampToJoinedRunnerUseCase.PostStampParam
    ): CommonEntity
}