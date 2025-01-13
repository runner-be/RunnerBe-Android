package com.applemango.runnerbe.repository

import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.CommonEntity
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun patchAlarm(userId: Int, pushOn : Boolean) : CommonEntity
    fun getAlarms(): Flow<List<AlarmEntity>>
}