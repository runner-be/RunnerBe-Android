package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.Alarm
import com.applemango.runnerbe.data.dto.AlarmsDto
import com.applemango.runnerbe.data.mapper.AlarmMapper
import com.applemango.runnerbe.entity.AlarmEntity
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToData(input: List<AlarmEntity>): AlarmsDto {
        val result = input.map {
            Alarm(
                it.alarmId,
                it.userId,
                it.createdAt,
                it.title,
                it.content,
                it.whetherRead,
            )
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