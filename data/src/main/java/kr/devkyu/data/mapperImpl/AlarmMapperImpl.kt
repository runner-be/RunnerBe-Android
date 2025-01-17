package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.Alarm
import kr.devkyu.data.dto.AlarmsDto
import kr.devkyu.data.mapper.AlarmMapper
import com.applemango.runnerbe.entity.AlarmEntity
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToData(input: List<AlarmEntity>): kr.devkyu.data.dto.AlarmsDto {
        val result = input.map {
            kr.devkyu.data.dto.Alarm(
                it.alarmId,
                it.userId,
                it.createdAt,
                it.title,
                it.content,
                it.whetherRead,
            )
        }
        return kr.devkyu.data.dto.AlarmsDto(
            alarms = result
        )
    }

    override fun mapToDomain(input: kr.devkyu.data.dto.AlarmsDto): List<AlarmEntity> {
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