package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.new.PostingDetailDto
import com.applemango.runnerbe.data.mapper.PostingDetailMapper
import com.applemango.runnerbe.entity.PostingDetailEntity

class PostingDetailMapperImpl: PostingDetailMapper {
    override fun mapToData(input: PostingDetailEntity): PostingDetailDto {
        return PostingDetailDto(
            result = input.postingDetail
        )
    }

    override fun mapToDomain(input: PostingDetailDto): PostingDetailEntity {
        return PostingDetailEntity(
            input.result
        )
    }
}