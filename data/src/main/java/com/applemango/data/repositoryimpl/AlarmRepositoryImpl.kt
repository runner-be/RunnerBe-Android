package com.applemango.data.repositoryimpl

import com.applemango.data.mapper.CommonMapper
import com.applemango.data.network.UserDataStore
import com.applemango.data.network.api.GetAlarmsApi
import com.applemango.data.network.api.PatchAlarmApi
import com.applemango.domain.entity.AlarmEntity
import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val userDataStore: UserDataStore,
    private val patchAlarmApi: PatchAlarmApi,
    private val getAlarmsApi: GetAlarmsApi,
    private val commonMapper: CommonMapper,
): BaseRepository(), AlarmRepository {
    override suspend fun patchAlarm(pushOn: Boolean): CommonEntity {
        val userId = userDataStore.getUserId().first()
        return handleApiCall(
            apiCall = {
                /**
                 * TODO
                 * pushOn 함수 변환 과정 수정
                 */
                patchAlarmApi.patchAlarm(userId, pushOn.toString())
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getAlarms(): Flow<List<AlarmEntity>> {
        val response = getAlarmsApi.getAlarms()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return flow { body.alarms }
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}