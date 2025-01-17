package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.CommonDto
import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.entity.CommonEntity
import javax.inject.Inject

class CommonMapperImpl @Inject constructor(): CommonMapper {
    override fun mapToData(input: CommonEntity): CommonDto {
        return CommonDto()
    }

    override fun mapToDomain(input: CommonDto): CommonEntity {
        return CommonEntity(
            isSuccess = input.isSuccess,
            code = input.code,
            message = input.message
        )
    }
}