package com.applemango.data.mapperImpl

import com.applemango.data.dto.CommonDto
import com.applemango.data.mapper.CommonMapper
import com.applemango.domain.entity.CommonEntity
import javax.inject.Inject

class CommonMapperImpl @Inject constructor() :
    CommonMapper {
    override fun mapToData(input: CommonEntity): CommonDto {
        return CommonDto()
    }

    override fun mapToDomain(input: CommonDto): CommonEntity {
        return com.applemango.domain.entity.CommonEntity(
            isSuccess = input.isSuccess,
            code = input.code,
            message = input.message
        )
    }
}