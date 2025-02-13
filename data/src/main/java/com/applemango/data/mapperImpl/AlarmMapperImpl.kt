package com.applemango.data.mapperImpl

import com.applemango.data.dto.AlarmDto
import com.applemango.data.dto.Alarms
import com.applemango.data.dto.AlarmsDto
import com.applemango.data.mapper.AlarmMapper
import com.applemango.domain.entity.AlarmEntity
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToData(input: List<AlarmEntity>): AlarmsDto {
        val result = input.map {
            val alarm = AlarmDto(
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
            alarms = Alarms(result)
        )
    }

    override fun mapToDomain(input: AlarmsDto): List<AlarmEntity> {
        val result = input.alarms.alarms.map {
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