package kr.devkyu.data.mapperImpl

import kr.devkyu.data.mapper.AlarmMapper
import com.applemango.runnerbe.entity.AlarmEntity
import kr.devkyu.data.dto.Alarm
import kr.devkyu.data.dto.AlarmsDto
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToData(input: List<AlarmEntity>): AlarmsDto {
        val result = input.map {
            val alarm = Alarm(
                it.alarmId,
                it.userId,
                it.createdAt,
                it.title,
                it.content,
                it.whetherRead,
            )
            alarm
        }
        return AlarmsDto(
            alarms = result
        )
    }

    override fun mapToDomain(input: AlarmsDto): List<AlarmEntity> {
        val result = input.alarms.map {
            AlarmEntity(
                it.alarmId,
                it.userId,
                it.createdAt,
                it.title,
                it.content,
                it.whetherRead,
            )
        }
        return result
    }
}