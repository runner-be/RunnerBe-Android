package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.presentation.mapper.AlarmMapper
import com.applemango.runnerbe.presentation.model.AlarmModel
import javax.inject.Inject

class AlarmMapperImpl @Inject constructor(): AlarmMapper {
    override fun mapToDomain(input: AlarmModel): AlarmEntity {
        return AlarmEntity(
            alarmId = input.alarmId,
            userId = input.userId,
            createdAt = input.createdAt,
            title = input.title,
            content = input.content,
            whetherRead = input.whetherRead,
        )
    }

    override fun mapToPresentation(input: AlarmEntity): AlarmModel {
        return AlarmModel(
            alarmId = input.alarmId,
            userId = input.userId,
            createdAt = input.createdAt,
            title = input.title,
            content = input.content,
            whetherRead = input.whetherRead,
        )
    }
}