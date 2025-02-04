package com.applemango.domain.repository

import com.applemango.domain.entity.AlarmEntity
import com.applemango.domain.entity.CommonEntity
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun patchAlarm(pushOn : Boolean) : CommonEntity
    fun getAlarms(): Flow<List<AlarmEntity>>
}