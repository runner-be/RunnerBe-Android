package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kr.devkyu.data.network.TokenSPreference
import kr.devkyu.data.network.UserDataStore
import kr.devkyu.data.network.api.GetAlarmsApi
import kr.devkyu.data.network.api.PatchAlarmApi
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

    override fun getAlarms(): Flow<List<AlarmEntity>> {
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