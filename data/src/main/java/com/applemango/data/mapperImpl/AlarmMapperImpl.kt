package com.applemango.data.mapperImpl

import com.applemango.data.dto.Alarm
import com.applemango.data.dto.AlarmsDto
import com.applemango.data.mapper.AlarmMapper
import com.applemango.domain.entity.AlarmEntity
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToData(input: List<com.applemango.domain.entity.AlarmEntity>): AlarmsDto {
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

    override fun mapToDomain(input: AlarmsDto): List<com.applemango.domain.entity.AlarmEntity> {
        val result = input.alarms.map {
            com.applemango.domain.entity.AlarmEntity(
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