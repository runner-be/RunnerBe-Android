package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnerEntity
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase

interface JoinedRunnerRepository {
    suspend fun attendanceAccession(postId: Int, request: AttendanceAccessionUseCase.AttendanceAccessionParam) : CommonEntity
    suspend fun postWhetherAccept(postId: Int, applicantId: Int, whetherAccept : String) : CommonEntity
    suspend fun getJoinedRunnerList(logId: Int): List<JoinedRunnerEntity>
    suspend fun postStampToJoinedRunner(logId: Int, stamp: WriteStampToJoinedRunnerUseCase.PostStampParam): CommonEntity
}