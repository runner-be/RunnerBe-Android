package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnersEntity
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase
import com.applemango.runnerbe.usecaseImpl.runninglog.WriteStampToJoinedRunnerUseCase

interface JoinedRunnerRepository {
    suspend fun attendanceAccession(postId: Int, request: AttendanceAccessionUseCase.AttendanceAccessionParam) : CommonEntity
    suspend fun postWhetherAccept(postId: Int, applicantId: Int, whetherAccept : String) : CommonEntity
    suspend fun getJoinedRunnerList(userId: Int, logId: Int): List<JoinedRunnersEntity>
    suspend fun postStampToJoinedRunner(userId: Int, logId: Int, stamp: WriteStampToJoinedRunnerUseCase.PostStampParam): CommonEntity
}