package kr.devkyu.data.mapperImpl

import kr.devkyu.data.dto.CommonDto
import kr.devkyu.data.mapper.CommonMapper
import com.applemango.runnerbe.entity.CommonEntity
import javax.inject.Inject

class CommonMapperImpl @Inject constructor():
    CommonMapper {
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